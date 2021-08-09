/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.config;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.IsagApiTokenService;
import com.isag.businessservice.IsagUserRoleService;
import com.isag.businessservice.PrivilegeService;
import com.isag.dto.IsagApiTokenDTO;
import com.isag.entity.Branch;
import com.isag.entity.IsagPrivilege;
import com.isag.entity.IsagUserRole;
import com.isag.general.GeneralService;
import static com.sun.jersey.core.header.LinkHeader.uri;
import com.sun.jersey.core.util.Priority;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author AhmedESSAM
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String REALM = "login@erp.isag-it.com";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private UserLoginData userLoginData;
    @EJB
    private GeneralService generalService;
    @EJB
    private IsagApiTokenService isagApiTokenService;
    @EJB
    private IsagUserRoleService userRoleService;
    @EJB
    private PrivilegeService privilegeService;
    @EJB
    private BranchService branchService;

    public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> klass = type;
        while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
            // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
            for (final Method method : klass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    Annotation annotInstance = method.getAnnotation(annotation);
                    // TODO process annotInstance
                    methods.add(method);

                }
            }
            // move to the upper class in the hierarchy in search for more methods
            klass = klass.getSuperclass();
        }
        return methods;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the Authorization header from the request
        String authorizationHeader
                = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        // Extract the token from the Authorization header
        String token = authorizationHeader
                .substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            String uri = requestContext.getUriInfo().getAbsolutePath().toString();
            Integer branchSerial = Integer.valueOf(requestContext.getUriInfo().getPathSegments().get(requestContext.getUriInfo().getPathSegments().size()-1).toString()); //(Integer) requestContext.getProperty("branchSelected");

            // Validate the token
            if (!validateToken(token, uri, branchSerial)) {
                abortWithUnauthorized(requestContext);
                return;
            } else {
                requestContext.setProperty("userLogInData", getUserLoginData());
            }
        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE,
                                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
                        .build());
    }

    private Boolean validateToken(String token, String uri, Integer branchSerial) throws Exception {
        // Check if the token was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
        if (branchSerial == null || branchSerial <= 0) {
            return Boolean.FALSE;
        }
        Boolean verifyToken = generalService.verifyToken(token);
        if (verifyToken) {
            String tokenId = generalService.DecodeJWT(token);
            IsagApiTokenDTO isagApiTokenDTO = new IsagApiTokenDTO();
            isagApiTokenDTO.setToken(token);
            isagApiTokenDTO.setTokenId(tokenId);
            IsagApiTokenDTO apiTokenDTO = isagApiTokenService.getIsagApiTokenDTOByTokenAndTokenID(isagApiTokenDTO);
            List<Branch> branchList = branchService.getBranchCompanyBySerial(apiTokenDTO.getCompanyId(), branchSerial);
            if (branchList == null || branchList.isEmpty()) {
                return Boolean.FALSE;
            }
            getUserLoginData().setCompanyId(apiTokenDTO.getCompanyId());
            getUserLoginData().setBranchId(branchList.get(0).getId());
            getUserLoginData().setUserId(apiTokenDTO.getCreatedBy());
            IsagUserRole userRole = userRoleService.findUserRoleByUserIdAndBranch(apiTokenDTO.getCreatedBy(), apiTokenDTO.getBranchId());
            String entity = uri.substring(uri.indexOf("rest"));
            entity = entity.substring(5);
            entity = entity.substring(0, entity.indexOf("/"));
            IsagPrivilege privilege = privilegeService.getUserPrivilegeOnScreen(userRole.getRoleId().getId(), entity);
            if (privilege == null || privilege.getView() == null || !privilege.getView()) {
                return Boolean.FALSE;
            }
            return verifyToken;
        }
        return verifyToken;
    }

    /**
     * @return the userLoginData
     */
    public UserLoginData getUserLoginData() {
        if (userLoginData == null) {
            userLoginData = new UserLoginData();
        }
        return userLoginData;
    }

    /**
     * @param userLoginData the userLoginData to set
     */
    public void setUserLoginData(UserLoginData userLoginData) {
        this.userLoginData = userLoginData;
    }
}
