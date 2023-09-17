/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.SchoolStagesGuideDTO;
import com.isag.dto.StatesDTO;
import com.isag.dto.StudentDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.Student;
import com.isag.entity.IgmaUser;
import com.isag.entity.SchoolStagesGuide;
import com.isag.entity.States;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Stateless
public class StudentServiceImpl implements StudentService {
    
    @EJB
    private GenericDAO dao;
    @EJB
    private IgmaUserLoginService isagUserLoginService;
    @EJB
    private IgmaApiTokenService isagApiTokenService;
    @EJB
    private StudentMonthsService studentMonthsService;
    @EJB
    ExamsService examsService;
    
    @Override
    public APIStudentDataDTO ResetPassword(APIStudentDataDTO aPIStudentDataDTO) {
        Student student = null;
        if (aPIStudentDataDTO.getId() != null ) {
            student = dao.findEntityById(Student.class, aPIStudentDataDTO.getId());
        }
        
         student.setMacId(null);
         dao.updateEntity(student);
         aPIStudentDataDTO.setMsg("تم الحفظ بنجاح");
         aPIStudentDataDTO.setMsgType("success");
         return aPIStudentDataDTO;
            
//            if (aPIStudentDataDTO.getId() != null && aPIStudentDataDTO.getPassword() != null) {
//            student = dao.findEntityById(Student.class, aPIStudentDataDTO.getId());
//        }
//        if (aPIStudentDataDTO.getPassword().equals(student.getPassword())) {
//            student.setMacId(null);
//            dao.updateEntity(student);
//            msg.setMsg("تم الحفظ بنجاح");
//            msg.setMsgType("success");
//            return msg;
//        } else {
//            msg.setMsg("خطأ فى الباسورد ");
//            msg.setMsgType("fail");
//            return msg;
//        }
        
    }
    
    @Override
    public StudentDTO authenticateUser(String userName, String password, Integer branchId, Integer companyId) {
        StudentDTO dTO = new StudentDTO();
        Map<String, Object> params = new HashMap();
        params.put("userName", userName);
        params.put("password", password);
        params.put("branchId", branchId);
        params.put("companyId", companyId);
        Student student = dao.findEntityByQuery("SELECT e FROM Student e WHERE (e.userName = :userName or e.mobile = :userName or e.email = :userName) AND e.password = :password AND e.branchId.id = :branchId AND e.companyId.id = :companyId ", params);
        if (student != null) {
            dTO = mapTODTO(student, dTO);
        }
        return dTO;
    }
    
    @Override
    public String findStudentPassword(Integer studentId) {
        StudentDTO dTO = new StudentDTO();
        Map<String, Object> params = new HashMap();
        params.put("studentId", studentId);
        String password = dao.findEntityByQuery("SELECT e.password FROM Student e WHERE  AND e.id = :studentId ", params);
        if (password != null) {
            return password;
        }
        return "";
    }
    
    public Boolean validateUser(APIStudentDataDTO aPIStudentDataDTO) {
        Map<String, Object> params = new HashMap();
        StringBuilder query = new StringBuilder();
        params.put("userName", aPIStudentDataDTO.getUserName());
        params.put("mobile", aPIStudentDataDTO.getMobile());
        params.put("email", aPIStudentDataDTO.getEmail());
        params.put("branchId", aPIStudentDataDTO.getBranchId());
        params.put("companyId", aPIStudentDataDTO.getCompanyId());
        
        query.append("SELECT e FROM Student e WHERE (e.userName = :userName or e.mobile = :mobile or e.email = :email) AND e.branchId.id = :branchId AND e.companyId.id = :companyId");
        if (aPIStudentDataDTO.getId() != null) {
            params.put("id", aPIStudentDataDTO.getId());
            query.append(" AND e.id not in ( :id ) ");
        }
        List<Student> student = dao.findListByQuery(query.toString(), params);
        if (student != null && !student.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    public Boolean validateUserName(APIStudentDataDTO aPIStudentDataDTO) {
        Map<String, Object> params = new HashMap();
        StringBuilder query = new StringBuilder();
        params.put("userName", aPIStudentDataDTO.getUserName());
        query.append("SELECT e FROM Student e WHERE e.userName = :userName");
        List<Student> student = dao.findListByQuery(query.toString(), params);
        if (student != null && !student.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    public Boolean validateMobile(APIStudentDataDTO aPIStudentDataDTO) {
        Map<String, Object> params = new HashMap();
        StringBuilder query = new StringBuilder();
        params.put("mobile", aPIStudentDataDTO.getMobile());
        query.append("SELECT e FROM Student e WHERE e.mobile = :mobile");
        List<Student> student = dao.findListByQuery(query.toString(), params);
        if (student != null && !student.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    public Boolean validateEmail(APIStudentDataDTO aPIStudentDataDTO) {
        Map<String, Object> params = new HashMap();
        StringBuilder query = new StringBuilder();
        params.put("email", aPIStudentDataDTO.getEmail());
        query.append(" SELECT e FROM Student e WHERE e.email = :email ");
        List<Student> student = dao.findListByQuery(query.toString(), params);
        if (student != null && !student.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    @Override
    public APIStudentDataDTO save(APIStudentDataDTO aPIStudentDataDTO) {
        APIStudentDataDTO msg = new APIStudentDataDTO();
        Student student = null;
        if (aPIStudentDataDTO.getId() != null) {
            student = dao.findEntityById(Student.class, aPIStudentDataDTO.getId());
        }
        if (aPIStudentDataDTO != null && aPIStudentDataDTO.getUserName() != null
                && aPIStudentDataDTO.getPassword() != null
                && aPIStudentDataDTO.getEmail() != null
                && aPIStudentDataDTO.getMobile() != null
                && aPIStudentDataDTO.getSchoolStagesId() != null
                && aPIStudentDataDTO.getStateId() != null) {
            if (validateUserName(aPIStudentDataDTO)) {
                if (validateMobile(aPIStudentDataDTO)) {
                    if (validateEmail(aPIStudentDataDTO)) {
                        student = mapAPIToEntity(aPIStudentDataDTO, student);
                        dao.updateEntity(student);
                        msg.setMsg("تم الحفظ بنجاح");
                        msg.setMsgType("success");
                        return msg;
                    } else {
                        msg.setMsg(" الايميل مستخدم بالفعل");
                        msg.setMsgType("fail");
                        return msg;
                    }
                } else {
                    msg.setMsg(" الهاتف مستخدم بالفعل");
                    msg.setMsgType("fail");
                    return msg;
                }
            } else {
                msg.setMsg(" الاسم مستخدم بالفعل");
                msg.setMsgType("fail");
                return msg;
            }
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }
    
    @Override
    public StudentDTO addStudent(StudentDTO studentDTO, IgmaUser isagUser) {
        // Add method which includes the 4 tables data in .
        Student entity = mapFromDTO(studentDTO, isagUser);
        entity = dao.updateEntity(entity);
        studentDTO = mapTODTO(entity, studentDTO);
        
        return studentDTO;
        
    }
    
    @Override
    public List<Student> getAllStudentsListByBranchId(Integer branchId) {
        
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<StudentDTO> dTOList = new ArrayList<>();
        List<Student> list = dao.findListByQuery("SELECT d FROM Student d WHERE d.branchId.id=:branchId", params);
        return list;
    }
    
    @Override
    public List<StudentDTO> getStudentbySchoolStagesGuideId(Integer schoolStagesId) {
        
        Map<String, Object> params = new HashMap();
        params.put("schoolStagesId", schoolStagesId);
        StringBuilder query = new StringBuilder();
        query.append(" SELECT i FROM Student i WHERE i.schoolStagesId.id =:schoolStagesId ");
        List<StudentDTO> dTOList = new ArrayList<>();
        List<Student> students = dao.findListByQuery(query.toString(), params);
        returnListDTO(students, dTOList);
        return dTOList;
        
    }
    
    @Override
    public List<APIStudentDataDTO> getStudentbyBranchId(Integer branchId, Integer schoolStagesId, Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        StringBuilder query = new StringBuilder();
        query.append(" SELECT i FROM Student i WHERE i.branchId.id =:branchId ");
        if (schoolStagesId != null) {
            params.put("schoolStagesId", schoolStagesId);
            query.append(" AND i.schoolStagesId.id = :schoolStagesId");
        }
        if (id != null) {
            params.put("id", id);
            query.append(" AND i.id = :id ");
        }
        query.append(" order by i.id desc ");
        List<Student> students = dao.findListByQuery(query.toString(), params);
        return returnListDTOAPI(students);
    }
    
    @Override
    public List<StudentDTO> getAllStudentsListByParentId(Integer parentId) {
        
        Map<String, Object> params = new HashMap();
        params.put("parentId", parentId);
        List<StudentDTO> dTOList = new ArrayList<>();
        List<Student> list = dao.findListByQuery("SELECT d FROM Student d WHERE d.parentId.id=:parentId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public List<StudentDTO> getAllStudentsListByParentAndchields(Integer branchId, Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("parentId", parentId);
        List<StudentDTO> dTOList = new ArrayList<>();
        List<Student> list = dao.findListByQuery("SELECT e FROM Student e JOIN e.parentId e2 WHERE e.branchId.id = :branchId AND e2.parentId.id=:parentId or e.parentId.id=:parentId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public List<Integer> getAllStudentsListByParentAndchield(Integer branchId, Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("parentId", parentId);
        // query.append("SELECT e.id FROM InvOrganizationSite e LEFT JOIN e.parent e2 WHERE e.branchId.id = :branchId AND e.type = 5 AND (e.parent.id =: parentId OR e.parent.id = e2.id)");

        List<Integer> list = dao.findListByQuery("SELECT e.id FROM Student e LEFT JOIN e.parentId c WHERE e.branchId.id = :branchId AND ( e.parentId.id = :parentId OR e.parentId.id= c.id )", params);
        return list;
    }
    
    @Override
    public List<StudentDTO> getAllStudentsListByCompanyIdAndBranchId(Integer companyId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("companyId", companyId);
        List<StudentDTO> dTOList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Student d WHERE d.companyId.id = :companyId AND  d.branchId.id = :branchId ");
        List<Student> list = dao.findListByQuery(builder.toString(), params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public List<StudentDTO> getAllStudentsListByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        List<StudentDTO> dTOList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Student d WHERE d.companyId.id = :companyId ");
        List<Student> list = dao.findListByQuery(builder.toString(), params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public List<StudentDTO> getAllStudentsListByCompanyIdAndRemType(Integer companyId, Integer remType) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        params.put("remType", remType);
        List<StudentDTO> dTOList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Student d WHERE d.companyId.id = :companyId AND d.remType = :remType");
        List<Student> list = dao.findListByQuery(builder.toString(), params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public List<StudentDTO> getAllStudentsListByRemCompanyId(Integer remCompany) {
        Map<String, Object> params = new HashMap();
        params.put("remCompany", remCompany);
        List<StudentDTO> dTOList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Student d WHERE d.remCompany.id = :remCompany ");
        List<Student> list = dao.findListByQuery(builder.toString(), params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public List<StudentDTO> getAllStudentsListByRemPointsId(Integer remPoints, Integer remTrafficId) {
        //RemStudentShiftDetailEdit
        Map<String, Object> params = new HashMap();
        params.put("remPoints", remPoints);
        params.put("remTrafficId", remTrafficId);
        List<StudentDTO> dTOList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT r.studentId FROM RemStudentShiftDetail r WHERE r.studentId.remPoints.id = :remPoints AND r.remStudentShiftId.remTrafficId.id = :remTrafficId ");
        List<Student> list = dao.findListByQuery(builder.toString(), params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public Boolean getAllStudentsIsSuperVisor(Integer studentId) {
        Map<String, Object> params = new HashMap();
        params.put("studentId", studentId);
        Integer count = ((Long) dao.findEntityByQuery("SELECT count(e) FROM Student e WHERE e.parentId.id = :studentId AND e.id != :studentId", params)).intValue();
        if (count > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
        
    }
    
    @Override
    public List<StudentDTO> getAllStudentsListByBranchIdAndHrSalaryStructure(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<StudentDTO> dTOList = new ArrayList<>();
        List<Student> list = dao.findListByQuery("SELECT e FROM Student e WHERE  e.branchId.id=:branchId and e.id NOT IN (SELECT h.studentId.id FROM HrSalaryStructure h )", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public StudentDTO findStudentId(Integer id) {
        Student student = dao.findEntityById(Student.class, id);
        StudentDTO dTO = new StudentDTO();
        return mapTODTO(student, dTO);
    }
    
    @Override
    public StudentDTO findStudentLoginData(Integer id) {
        Student entity = dao.findEntityById(Student.class, id);
        StudentDTO dTO = new StudentDTO();
        dTO.setId(entity.getId());
        dTO.setEmail(entity.getEmail());
        dTO.setPassword(entity.getPassword());
        dTO.setCode(entity.getCode());
        return dTO;
    }
    
    @Override
    public StudentDTO addStudent(StudentDTO student) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<Student> findStudentByCode(Integer companyId, Integer code) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("code", code);
        return dao.findListByQuery("SELECT e FROM Student e WHERE e.companyId.id = :companyId And e.code = :code", params);
    }
    
    @Override
    public StudentDTO findStudent(Integer departmentId) {
        Student student = dao.findEntityById(Student.class, departmentId);
        StudentDTO studentDTO = mapTODTO(student, null);
        return studentDTO;
    }
    
    @Override
    public StudentDTO findStudentByMobile(String mobile, Integer branchId) {
        
        Map<String, Object> params = new HashMap<>();
        
        StudentDTO studentDTO = null;
        
        params.put("mobile", mobile);
        params.put("branchId", branchId);
        
        List<Student> list = dao.findListByQuery("SELECT e FROM Student e WHERE e.branchId.id = :branchId And e.mobile = :mobile", params);
        if (list != null && !list.isEmpty()) {
            studentDTO = mapTODTO(list.get(0), null);
            return studentDTO;
        }
        
        return studentDTO;
    }
    
    @Override
    public StudentDTO updateStudentMacId(StudentDTO studentDTO) {
        Student student = dao.findEntityById(Student.class, studentDTO.getId());
        student.setMacId(studentDTO.getMacId());
        dao.updateEntity(student);
        return mapTODTO(student, studentDTO);
    }
    
    @Override
    public StudentDTO updateStudent(StudentDTO student) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void deleteStudent(StudentDTO student) {
        Student entity = new Student();
        entity.setId(student.getId());
        dao.deleteEntity(entity);
    }
    
    @Override
    public void deleteStudent(Integer student) {
        Student entity = new Student();
        isagUserLoginService.deleteByStudentId(student);
        isagApiTokenService.deleteByStudentId(student);
        studentMonthsService.deleteStudentMontsByStudent(student);
        examsService.deleteExamsListForStudent(student);
        entity.setId(student);
        dao.deleteEntity(entity);
    }
    
    @Override
    public List<StudentDTO> getAllStudentsListByCompanyId(Integer companyId, Integer branchId) {
        
        Map<String, Object> params = new HashMap<>();
        List<StudentDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        params.put("branchId", branchId);
        List<Student> list = dao.findListByQuery("SELECT i.studentId FROM IsagUser i WHERE  i.studentId.companyId.id = :companyId AND i.branchId.id = :branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public List<StudentDTO> getAllStudentsListByCompanyIdByType(Integer branchId, Integer remType) {
        
        Map<String, Object> params = new HashMap<>();
        List<StudentDTO> dTOList = new ArrayList<>();
        params.put("remType", remType);
        params.put("branchId", branchId);
        List<Student> list = dao.findListByQuery("SELECT e FROM Student e WHERE  e.branchId.id=:branchId AND e.remType = :remType", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    /*@Override
    public List<EmployeeDTO> findAllStudentShift(Integer shiftId) {
        //RemEmployeeShiftDetail
        if (shiftId != null) {
            List<Employee> studenList = new ArrayList<>();
            List<EmployeeDTO> studentDTOList = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            String query = "";
            params.put("shiftId", shiftId);
            query = "SELECT r FROM RemEmployeeShiftDetail r WHERE r.remEmployeeShiftId.id = :shiftId";
            List<RemEmployeeShiftDetail> employeeShiftDetaileseList = dao.findListByQuery(query, params);
            for (RemEmployeeShiftDetail detail : employeeShiftDetaileseList) {
                studenList.add(detail.getEmployeeId());
            }
            if (studenList == null || studenList.isEmpty()) {
                return null;
            } else {
                returnListDTO(studenList, studentDTOList);
                return studentDTOList;
            }
        } else {
            return null;
        }
    }
     */
    @Override
    public List<StudentDTO> getAllStudentsListByParentANDUserLogin(Integer studentId) {
        
        Map<String, Object> params = new HashMap<>();
        List<StudentDTO> dTOList = new ArrayList<>();
        params.put("studentId", studentId);
        List<Student> list = dao.findListByQuery("SELECT e FROM Student e WHERE e.parentId.id=:studentId or e.id = :studentId ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public List<Student> getAllStudentListByCompanyId(Integer companyId) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Student> list = dao.findListByQuery("SELECT e FROM Student e WHERE  e.companyId.id=:companyId", params);
        return list;
    }
    
    @Override
    public StudentDTO mapTODTO(Student entity, StudentDTO dTO) {
        if (dTO == null) {
            dTO = new StudentDTO();
        }
        initMapToDTO(dTO, entity);
        
        return dTO;
    }
    
    private StudentDTO initMapToDTO(StudentDTO dTO, Student entity) {
        
        if (dTO == null) {
            dTO = new StudentDTO();
        }
        dTO.setUserName(entity.getUserName());
        dTO.setPassword(entity.getPassword());
        dTO.setId(entity.getId());
        dTO.setEmail(entity.getEmail());
        dTO.setFirstName(entity.getFirstName());
        dTO.setFatherName(entity.getFatherName());
        dTO.setGrandFatherName(entity.getGrandFatherName());
        dTO.setFamilyName(entity.getFamilyName());
        dTO.setCode(entity.getCode());
        dTO.setMobile(entity.getMobile());
        dTO.setImage(entity.getImage());
        dTO.setFatherMobile(entity.getFatherMobile());
        dTO.setGender(entity.getGender());
        dTO.setImagePath(entity.getImagePath());
        dTO.setTypeLogin(entity.getTypeLogin());
        dTO.setMacId(entity.getMacId());
        dTO.setMacMobileId(entity.getMacMobileId());
        dTO.setIpAddress(entity.getIpAddress());
        dTO.setFatherJob(entity.getFatherJob());
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        
        if (entity.getSchoolStagesId() != null) {
            SchoolStagesGuideDTO schoolStagesGuideDTO = new SchoolStagesGuideDTO();
            schoolStagesGuideDTO.setName(entity.getSchoolStagesId().getName());
            schoolStagesGuideDTO.setCode(entity.getSchoolStagesId().getCode());
            schoolStagesGuideDTO.setId(entity.getSchoolStagesId().getId());
            dTO.setSchoolStagesId(schoolStagesGuideDTO);
        }
        if (entity.getStateId() != null) {
            StatesDTO statesDTO = new StatesDTO();
            statesDTO.setName(entity.getStateId().getName());
            statesDTO.setId(entity.getStateId().getId());
            dTO.setStateId(statesDTO);
            
        }
        return dTO;
        
    }
    
    private APIStudentDataDTO initMapToAPIDTO(APIStudentDataDTO dTO, Student entity) {
        
        if (dTO == null) {
            dTO = new APIStudentDataDTO();
        }
        dTO.setUserName(entity.getUserName());
        dTO.setPassword(entity.getPassword());
        dTO.setId(entity.getId());
        dTO.setFullName(entity.getFullName());
        dTO.setEmail(entity.getEmail());
        dTO.setFirstName(entity.getFirstName());
        dTO.setFatherName(entity.getFatherName());
        dTO.setGrandFatherName(entity.getGrandFatherName());
        dTO.setFamilyName(entity.getFamilyName());
        dTO.setCode(entity.getCode());
        dTO.setMobile(entity.getMobile());
        dTO.setFatherMobile(entity.getFatherMobile());
        dTO.setGender(entity.getGender());
        dTO.setImagePath(entity.getImagePath());
        dTO.setTypeLogin(entity.getTypeLogin());
        dTO.setMacId(entity.getMacId());
        dTO.setMacMobileId(entity.getMacMobileId());
        dTO.setIpAddress(entity.getIpAddress());
        dTO.setFatherJob(entity.getFatherJob());
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setGovernorateId(entity.getBranchId().getId());
        dTO.setImage(entity.getImage());
        dTO.setPeriodOrderId55(entity.getPeriodOrderId());
        dTO.setPeriodName(entity.getPeriodName());
        if (entity.getSchoolStagesId() != null) {
            dTO.setSchoolStagesId(entity.getSchoolStagesId().getId());
            dTO.setSchoolStagesName(entity.getSchoolStagesId().getName());
        }
        if (entity.getStateId() != null) {
            dTO.setStateId(entity.getStateId().getId());
            dTO.setStateName(entity.getStateId().getName());
//            dTO.setGovernorateId(entity.getStateId().getGovernorateId().getId());

        }
        
        return dTO;
        
    }
    
    public Student mapFromDTO(StudentDTO dTO, IgmaUser isagUser) {
        Student entity = new Student();
        
        entity.setUserName(dTO.getUserName());
        entity.setPassword(dTO.getPassword());
        entity.setId(dTO.getId());
        entity.setEmail(dTO.getEmail());
        entity.setCode(dTO.getCode());
        entity.setFullName(dTO.getFirstName() + " " + dTO.getFatherName() + " " + dTO.getGrandFatherName() + " " + dTO.getFamilyName());
        entity.setFirstName(dTO.getFirstName());
        entity.setFatherName(dTO.getFatherName());
        entity.setGrandFatherName(dTO.getGrandFatherName());
        entity.setFamilyName(dTO.getFamilyName());
        entity.setMobile(dTO.getMobile());
        entity.setFatherMobile(dTO.getFatherMobile());
        entity.setGender(dTO.getGender());
        entity.setImagePath(dTO.getImagePath());
        entity.setTypeLogin(dTO.getTypeLogin());
        entity.setMacId(dTO.getMacId());
        entity.setMacMobileId(dTO.getMacMobileId());
        entity.setIpAddress(dTO.getIpAddress());
        entity.setFatherJob(dTO.getFatherJob());
        entity.setImage(dTO.getImage());
        
        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
            
        } else {
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }
        
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }
    
    @Override
    public void deleteIdfromFellowsQualifications(Integer studentId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<APIStudentDataDTO> returnListDTOAPI(List<Student> studentList) {
        List<APIStudentDataDTO> list = new ArrayList<>();
        for (Student entity : studentList) {
            list.add(initMapToAPIDTO(null, entity));
        }
        return list;
    }
    
    private Student mapAPIToEntity(APIStudentDataDTO dTO, Student entity) {
        if (entity == null) {
            entity = new Student();
            IgmaUser isagUser = new IgmaUser();
            isagUser.setId(61);
            
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
            
            Branch branch = new Branch();
            branch.setId(dTO.getBranchId());
            entity.setBranchId(branch);
            
            IgmaCompany isagCompany = new IgmaCompany();
            isagCompany.setId(dTO.getCompanyId());
            entity.setCompanyId(isagCompany);
        }
        
        entity.setFirstName(dTO.getFirstName());
        entity.setFatherName(dTO.getFatherName());
        entity.setGrandFatherName(dTO.getGrandFatherName());
        entity.setFamilyName(dTO.getFamilyName());
        entity.setFullName(dTO.getFirstName() + " " + dTO.getFatherName() + " " + dTO.getGrandFatherName() + " " + dTO.getFamilyName());
        entity.setCode(dTO.getCode());
        entity.setEmail(dTO.getEmail());
        entity.setFatherMobile(dTO.getFatherMobile());
        entity.setGender(dTO.getGender());
        entity.setImagePath(dTO.getImagePath());
        entity.setMobile(dTO.getMobile());
        entity.setPassword(dTO.getPassword());
        entity.setUserName(dTO.getUserName());
        entity.setTypeLogin(dTO.getTypeLogin());
        entity.setMacId(dTO.getMacId());
        entity.setMacMobileId(dTO.getMacMobileId());
        entity.setIpAddress(dTO.getIpAddress());
        entity.setFatherJob(dTO.getFatherJob());
        entity.setImage(dTO.getImage());
        
        if (dTO.getSchoolStagesId() != null) {
            SchoolStagesGuide schoolStagesGuide = new SchoolStagesGuide();
            schoolStagesGuide.setId(dTO.getSchoolStagesId());
            entity.setSchoolStagesId(schoolStagesGuide);
        }
        
        if (dTO.getStateId() != null) {
            States states = new States();
            states.setId(dTO.getStateId());
            entity.setStateId(states);
        }
        
        return entity;
    }
    
    public void returnListDTO(List<Student> students, List<StudentDTO> dTOList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<APIStudentDataDTO> getAllStudentsListByMacId(String macId) {
        Map<String, Object> params = new HashMap();
        params.put("macId", macId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Student d WHERE d.macId = :macId ");
        List<Student> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTOAPI(list);
    }
    
}
