/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package importData;

import com.isag.businessservice.BranchService;
import com.isag.isag.*;
import com.isag.businessservice.CostCenterService;
import com.isag.businessservice.CurrencyService;
import com.isag.businessservice.EmployeeService;
import com.isag.businessservice.GeneraljournalDetailsService;
import com.isag.businessservice.GeneraljournalService;
import com.isag.businessservice.GeneraljournalServiceDTO;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.GlAdminUnitService;
import com.isag.businessservice.GlBankService;
import com.isag.businessservice.GlBankTransactionService;
import com.isag.businessservice.GlYearService;
import com.isag.businessservice.InstContractDetailsService;
import com.isag.businessservice.InstContractService;
import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.InvGlobalService;
import com.isag.businessservice.InvGroupService;
import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvNoticeService;
import com.isag.businessservice.InvOpeningBalanceItemDetailService;
import com.isag.businessservice.InvOpeningBalanceItemService;
import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.businessservice.InvPurchaseInvoiceService;
import com.isag.businessservice.InvTransferService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.RscWorkUnitsGuideService;
import com.isag.businessservice.SymbolService;
import com.isag.converter.APIBranchConverter;
import com.isag.converter.BranchConverter;
import com.isag.define.AccountClassEnum;
import com.isag.define.AccountGroupEnum;
import com.isag.define.AdminUnitDependantEnum;
import com.isag.define.AssisstantAccountDependantEnum;
import com.isag.define.CostCenterDependantEnum;
import com.isag.dto.APIBranchDTO;
import com.isag.dto.GeneralJournalDTO;
import com.isag.dto.GlYearDTO;
import com.isag.dto.InstContractDetailDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.Branch;
import com.isag.entity.CostCenter;
import com.isag.entity.Currency;
import com.isag.entity.Employee;
import com.isag.entity.GeneralJournal;
import com.isag.entity.GeneralJournalDetails;
import com.isag.entity.GeneralSymbol;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.GlBank;
import com.isag.entity.GlBankTransaction;
import com.isag.entity.GlBankTransactionDetail;
import com.isag.entity.GlYear;
import com.isag.entity.InstContract;
import com.isag.entity.InstContractDetail;
import com.isag.entity.InvGroup;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import com.isag.entity.InvNotice;
import com.isag.entity.InvOpenningBalanceItem;
import com.isag.entity.InvOpenningBalanceItemDetail;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.IsagCompany;
import com.isag.entity.RscWorkUnitsGuide;
import com.isag.entity.Symbol;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.olap4j.impl.ArrayMap;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author hq004
 */
@Named(value = "importMB")
@ViewScoped
public class ImportMB extends Basic implements Serializable {

    private Integer index = 0;
    private UploadedFile documentFile;
    private UploadedFile costCenterFile;
    private UploadedFile invItemFile;
    private UploadedFile adminUnitFile;
    private UploadedFile yearFile;
    private UploadedFile currencyFile;
    private UploadedFile invOpeningBalanceFile;
    private UploadedFile customerFile;
    private UploadedFile glAccountFile;
    private UploadedFile GeneralJournalHeadFile;
    private UploadedFile ContractFile;

    private InputStream inputStreamUploadDocument;
    private InputStream inputStreamUploadCostCenter;
    private InputStream inputStreamUploadAdminUnit;
    private InputStream inputStreamUploadYear;
    private InputStream inputStreamUploadCurrency;
    private InputStream inputStreamUploadGlAccount;
    private InputStream inputStreamUploadGeneralJournalHead;
    private InputStream inputStreamUploadInvItem;
    private InputStream inputStreamUploadinvOpeningBalance;
    private InputStream inputStreamUploadCustomer;
    private InputStream inputStreamUploadContract;

    private UserData userData;

    private List<APIBranchDTO> brachListFrom;
    private List<APIBranchDTO> brachListTo;

    private APIBranchDTO brachListSelectFrom;
    private APIBranchDTO brachListSelectTo;

    private APIBranchConverter branchConverterFrom;
    private APIBranchConverter branchConverterTo;

    private Integer moduleId = 1;
    private Integer level;
    private Integer progress;
    private Boolean disable;

    public ImportMB() {
    }

    @EJB
    private CostCenterService costCenterService;
    @EJB
    private GlAdminUnitService adminUnitService;
    @EJB
    private SymbolService symbolService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private GlYearService glYearService;
    @EJB
    private GlAccountService glAccountService;
    @EJB
    private GeneraljournalService generaljournalService;
    @EJB
    private GeneraljournalServiceDTO generaljournalServiceDTO;
    @EJB
    private GeneraljournalDetailsService generaljournalDetailsService;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private InvDelegatorService invDelegatorService;
    @EJB
    private InvOpeningBalanceItemDetailService invOpeningBalanceItemDetailService;
    @EJB
    private InvOpeningBalanceItemService invOpeningBalanceItemService;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    private InvTransferService invTransferService;
    @EJB
    private InstContractService instContractService;
    @EJB
    private InstContractDetailsService contractDetailsService;
    @EJB
    private GlBankTransactionService glBankTransactionService;
    @EJB
    private GlBankService glBankService;
    @EJB
    private BranchService branchService;
    @EJB
    private RscWorkUnitsGuideService rscWorkUnitsGuideService;
    @EJB
    private EmployeeService employeeService;
    @EJB
    private InvGroupService invGroupService;
    @EJB
    private InvNoticeService invNoticeService;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
    }

    public void OpenDlg(String dlgvar) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('" + dlgvar + "').show();");
    }

    // CostCenter Code
    public void readCostCenter() throws BiffException, IOException, Exception {
        setInputStreamUploadCostCenter(getCostCenterFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadCostCenter());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        importCostCenter(sheet);
        workbook.close();
    }

    public void importCostCenter(Sheet sheet) throws NumberFormatException {
        Integer parentCode = null;
        CostCenter costCenter;
        Map<Integer, CostCenter> itemMap = new ArrayMap<>();
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                costCenter = new CostCenter();
                Cell parent = initCostCenterData(sheet, i, costCenter);
                costCenterParent(parent, itemMap, costCenter, parentCode);
                costCenterService.addCostCenter(costCenter);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public Cell initCostCenterData(Sheet sheet, int i, CostCenter costCenter) throws NumberFormatException {
        Cell code = sheet.getCell(1, i);
        Cell name = sheet.getCell(2, i);
        Cell parent = sheet.getCell(4, i);
        Cell level = sheet.getCell(5, i);
        Cell active = sheet.getCell(7, i);
        costCenter.setBranchId(getUserData().getUserBranch());
        costCenter.setCode(Integer.parseInt(code.getContents()));
        costCenter.setName(name.getContents());
        costCenter.setLevel(Integer.parseInt(level.getContents()));
        costCenter.setCreatedBy(getUserData().getUser());
        costCenter.setCreationDate(new Date());
        costCenter.setCompanyId(getUserData().getCompany());
        if ("0".equals(active.getContents())) {
            costCenter.setActive(Boolean.TRUE);
        } else {
            costCenter.setActive(Boolean.FALSE);
        }
        return parent;
    }

    public void costCenterParent(Cell parent, Map<Integer, CostCenter> itemMap, CostCenter costCenter, Integer parentCode) throws NumberFormatException {
        if (!parent.getContents().isEmpty()) {
            parentCode = Integer.parseInt(parent.getContents());
            CostCenter costCenterParent = null;
            if (itemMap.containsKey(parentCode)) {
                costCenterParent = itemMap.get(parentCode);
            } else {
                costCenterParent = costCenterService.findCostCenterByCode(parentCode, getUserData().getUserBranch().getId());
                itemMap.put(costCenterParent.getCode(), costCenterParent);
            }

            if (costCenterParent != null) {
                costCenter.setParent(costCenterParent);
            }
        }
    }

    // AdminUnit Code
    public void readAdminUnit() throws BiffException, IOException, Exception {
        setInputStreamUploadAdminUnit(getAdminUnitFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadAdminUnit());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        importAdminUnit(sheet);
        workbook.close();
    }

    public void importAdminUnit(Sheet sheet) throws NumberFormatException {
        Integer parentCode = null;
        GlAdminUnit item;
        Map<Integer, GlAdminUnit> itemMap = new ArrayMap<>();
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                item = new GlAdminUnit();
                Cell parent = initAdminUnitData(sheet, i, item);
                adminUnitParent(parent, itemMap, item, parentCode);
                adminUnitService.addAdminUnit(item);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public Cell initAdminUnitData(Sheet sheet, int i, GlAdminUnit item) throws NumberFormatException {
        Cell code = sheet.getCell(1, i);
        Cell name = sheet.getCell(2, i);
        Cell parent = sheet.getCell(4, i);
        Cell level = sheet.getCell(5, i);
        Cell active = sheet.getCell(7, i);
        item.setBranchId(getUserData().getUserBranch());
        item.setCode(Integer.parseInt(code.getContents()));
        item.setName(name.getContents());
        item.setLevel(Integer.parseInt(level.getContents()));
        item.setCreatedBy(getUserData().getUser());
        item.setCreationDate(new Date());
        item.setCompanyId(getUserData().getCompany());
        if ("0".equals(active.getContents())) {
            item.setActive(Boolean.TRUE);
        } else {
            item.setActive(Boolean.FALSE);
        }
        return parent;
    }

    public void adminUnitParent(Cell parent, Map<Integer, GlAdminUnit> itemMap, GlAdminUnit item, Integer parentCode) throws NumberFormatException {
        if (!parent.getContents().isEmpty()) {
            parentCode = Integer.parseInt(parent.getContents());
            GlAdminUnit itemParent = null;
            if (itemMap.containsKey(parentCode)) {
                itemParent = itemMap.get(parentCode);
            } else {
                itemParent = adminUnitService.findAdminUnitByCode(parentCode, getUserData().getUserBranch().getId());
                itemMap.put(itemParent.getCode(), itemParent);
            }

            if (itemParent != null) {
                item.setParent(itemParent);
            }
        }
    }

    // Document Code
    public void readDocument() throws BiffException, IOException, Exception {
        setInputStreamUploadDocument(getDocumentFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadDocument());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        importDocument(sheet);
        workbook.close();
    }

    public void importDocument(Sheet sheet) throws NumberFormatException {
        Symbol item;
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                item = new Symbol();
                initDocumentData(sheet, i, item);
                symbolService.addSymbol(item);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public void initDocumentData(Sheet sheet, int i, Symbol item) throws NumberFormatException {
        Cell code = sheet.getCell(2, i);
        Cell name = sheet.getCell(3, i);
        item.setSerial(Integer.parseInt(code.getContents()));
        item.setName(name.getContents());
        item.setCreatedBy(getUserData().getUser());
        item.setCreationDate(new Date());
        item.setCompanyId(getUserData().getCompany());
        GeneralSymbol generalSymbol = new GeneralSymbol();
        generalSymbol.setId(6);
        item.setGeneralSymbolId(generalSymbol);
    }

    // Year Code
    public void readYear() throws BiffException, IOException, Exception {
        setInputStreamUploadYear(getYearFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadYear());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        importYear(sheet);
        workbook.close();
    }

    public void importYear(Sheet sheet) throws NumberFormatException, ParseException {
        GlYear item;
        Map<Integer, GlYear> itemMap = new ArrayMap<>();
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                item = new GlYear();
                initYearData(sheet, i, item);
                glYearService.updateYear(item);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public void initYearData(Sheet sheet, int i, GlYear item) throws NumberFormatException, ParseException {
        Cell name = sheet.getCell(2, i);
        Cell dateFrom = sheet.getCell(3, i);
        Cell dateTo = sheet.getCell(4, i);
        item.setBranchId(getUserData().getUserBranch());
        item.setName(name.getContents());
        item.setYear(Integer.parseInt(name.getContents()));
        item.setCreatedBy(getUserData().getUser());
        item.setCreationDate(new Date());
        item.setCompanyId(getUserData().getCompany());
        if (!dateFrom.getContents().isEmpty()) {
            Date datef1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateFrom.getContents());
            item.setDateFrom(datef1);
        }

        if (!dateTo.getContents().isEmpty()) {
            Date datet1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateTo.getContents());
            item.setDateTo(datet1);
        }

    }

    // Currency Code
    public void readCurrency() throws BiffException, IOException, Exception {
        setInputStreamUploadCurrency(getCurrencyFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadCurrency());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        importCurrency(sheet);
        workbook.close();
    }

    public void importCurrency(Sheet sheet) throws NumberFormatException, ParseException {
        Currency item;
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                item = new Currency();
                initCurrencyData(sheet, i, item);
                currencyService.addCurrency(item);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public void initCurrencyData(Sheet sheet, int i, Currency item) throws NumberFormatException, ParseException {
        Cell serial = sheet.getCell(1, i);
        Cell name = sheet.getCell(2, i);
        Cell code = sheet.getCell(3, i);
        item.setName(name.getContents());
        item.setSerial(Integer.parseInt(serial.getContents()));
        item.setCode(code.getContents());
        item.setCreatedBy(getUserData().getUser());
        item.setCreationDate(new Date());
        item.setCompanyId(getUserData().getCompany());
    }

    // GlAccount Code
    // Must input Currency First
    public void readGlAccount() throws BiffException, IOException, Exception {
        setInputStreamUploadGlAccount(getGlAccountFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadGlAccount());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        importGlAccount(sheet);
        workbook.close();
    }

    public void importGlAccount(Sheet sheet) throws NumberFormatException {
        Integer parentCode = null;
        GlAccount item;
        Map<Integer, GlAccount> itemMap = new ArrayMap<>();
        Map<Integer, Currency> currencyMap = new ArrayMap<>();
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                item = new GlAccount();
                Cell parent = initGlAccountData(sheet, i, item, itemMap, currencyMap);
                glAccountParent(parent, itemMap, item, parentCode);

                glAccountService.addGlAccount(item);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public Cell initGlAccountData(Sheet sheet, int i, GlAccount item, Map<Integer, GlAccount> itemMap, Map<Integer, Currency> currencyMap) throws NumberFormatException {
        Cell accNumber = sheet.getCell(0, i);
        Cell name = sheet.getCell(1, i);
        Cell levalAcc = sheet.getCell(2, i);
        Cell type = sheet.getCell(3, i);
        Cell parent = sheet.getCell(4, i);
        Cell accClass = sheet.getCell(5, i);
        Cell accGroup = sheet.getCell(6, i);
        Cell currencyId = sheet.getCell(7, i);
        Cell shotCode = sheet.getCell(8, i);

        item.setAccNumber(Integer.parseInt(!accNumber.getContents().isEmpty() ? accNumber.getContents() : null));
        item.setName(!name.getContents().isEmpty() ? name.getContents() : null);
        item.setLevelAcc(!levalAcc.getContents().isEmpty() ? Integer.parseInt(levalAcc.getContents()) : null);
        if (!type.getContents().isEmpty()) {
            if ("2".equals(type.getContents()) || "1".equals(type.getContents())) {
                item.setType(0);
            } else if ("3".equals(type.getContents())) {
                item.setType(1);
            }
        }
        item.setShotCode(!shotCode.getContents().isEmpty() ? Integer.parseInt(shotCode.getContents()) : null);
        if (!accGroup.getContents().isEmpty()) {
            if ("1".equals(accGroup.getContents())) {
                item.setAccGroup(AccountGroupEnum.ASSETS);
            } else if ("2".equals(accGroup.getContents())) {
                item.setAccGroup(AccountGroupEnum.DEDUCTION);
            } else if ("3".equals(accGroup.getContents())) {
                item.setAccGroup(AccountGroupEnum.EXPENSES);
            } else if ("4".equals(accGroup.getContents())) {
                item.setAccGroup(AccountGroupEnum.INCOME);
            }
        }
        item.setStatus(Boolean.TRUE);
        item.setCostCenter(CostCenterDependantEnum.CC_OPTIONAL);
        item.setAdministrativeUnit(AdminUnitDependantEnum.ADMUNT_OPTIONAL);
        item.setAssistantAcc(AssisstantAccountDependantEnum.ASSACC_OPTIONAL);
        if (!accClass.getContents().isEmpty()) {
            if ("2".equals(accClass.getContents())) {
                item.setAccClass(AccountClassEnum.DEBIT);
            } else if ("1".equals(accClass.getContents())) {
                item.setAccClass(AccountClassEnum.CREDIT);
            }
        }
        item.setBranchId(getUserData().getUserBranch());
        item.setCreatedBy(getUserData().getUser());
        item.setCreationDate(new Date());
        item.setCompanyId(getUserData().getCompany());
        if (!currencyId.getContents().isEmpty()) {
            findCurrencyCode(currencyMap, item, Integer.parseInt(currencyId.getContents()));
        }

        return parent;
    }

    public void glAccountParent(Cell parent, Map<Integer, GlAccount> itemMap, GlAccount item, Integer parentCode) throws NumberFormatException {
        if (!parent.getContents().isEmpty()) {
            parentCode = Integer.parseInt(parent.getContents());
            GlAccount itemParent = null;
            if (itemMap.containsKey(parentCode)) {
                itemParent = itemMap.get(parentCode);
            } else {
                itemParent = glAccountService.getAccountNumber(parentCode, getUserData().getUserBranch().getId());
                itemMap.put(itemParent.getAccNumber(), itemParent);
            }

            if (itemParent != null) {
                item.setParentAcc(itemParent);
            }
        }
    }

    public void findCurrencyCode(Map<Integer, Currency> itemMap, GlAccount item, Integer currencyCode) throws NumberFormatException {
        Currency itemParent = null;
        if (itemMap.containsKey(currencyCode)) {
            itemParent = itemMap.get(currencyCode);
        } else {
            itemParent = currencyService.getCurrencyByCompanyAndCode(getUserData().getCompany().getId(), currencyCode);
            itemMap.put(itemParent.getSerial(), itemParent);
        }

        if (itemParent != null) {
            item.setCurrencyId(itemParent);
        }
    }

    // GeneralJournalHead Code
    public void readGeneralJournalHead() throws BiffException, IOException, Exception {
        setInputStreamUploadGeneralJournalHead(getGeneralJournalHeadFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadGeneralJournalHead());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheetHead = workbook.getSheet(0);
//        Sheet sheetDetail = workbook.getSheet(1);
//        importGeneralJournalHead(sheetHead, sheetDetail);
        importGeneralJournalHeadTasaheet(sheetHead);
        workbook.close();
    }

    public void importGeneralJournalHead(Sheet sheetHead, Sheet sheetDetail) throws NumberFormatException, ParseException, IOException, Exception {
        GeneralJournal item;
        Map<Integer, GeneralJournal> itemMap = new ArrayMap<>();
        for (int i = 1; i < sheetHead.getRows(); i++) {
            if (!sheetHead.getCell(0, i).getContents().isEmpty()) {
                item = new GeneralJournal();
                initGeneralJournalHeadData(sheetHead, i, item);
                GeneralJournal generalJournal = generaljournalService.addGeneralJournalFromExcel(item);
                itemMap.put(generalJournal.getSerial(), generalJournal);
            }
        }
        String msg = importGeneralJournalDetail(sheetDetail, itemMap);
        if (!msg.isEmpty()) {
            OpenDlg(msg);
        } else {
            OpenDlg("sucesses");
        }

        System.out.println("تم الحفظ بنجاح");
    }

    public void initGeneralJournalHeadData(Sheet sheet, int i, GeneralJournal item) throws NumberFormatException, ParseException {
        Cell yearCode = sheet.getCell(1, i);
        Cell serial = sheet.getCell(2, i);
        Cell documentType = sheet.getCell(3, i);
        Cell documentNumber = sheet.getCell(4, i);
        Cell dateTo = sheet.getCell(5, i);
        Cell postFlag = sheet.getCell(7, i);
        Cell generalStatement = sheet.getCell(8, i);

        List<GlYear> yearList = glYearService.findGlYearWithCode(getUserData().getUserBranch().getId(), Integer.parseInt(yearCode.getContents()));

        if (yearList != null && !yearList.isEmpty() && yearList.size() > 0) {
            item.setGlYear(yearList.get(0));
        }
        item.setSerial(Integer.parseInt(serial.getContents()));
        item.setGeneralDecument(Integer.parseInt(documentNumber.getContents()));
        Symbol generalType = symbolService.getOneSymbolBySerial(getUserData().getCompany().getId(), Integer.parseInt(documentType.getContents()));
        item.setGeneralType(generalType);
        if (!dateTo.getContents().isEmpty()) {
            Date datef1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateTo.getContents());
            item.setGeneralData(datef1);
        }
        if ("0".equals(postFlag.getContents())) {
            item.setPost_flag(false);
        } else {
            item.setPost_flag(true);
        }
        item.setBranchId(getUserData().getUserBranch());
        item.setCreatedBy(getUserData().getUser());
        item.setCreationDate(new Date());
        item.setCompanyId(getUserData().getCompany());
        item.setGeneralStatement(generalStatement.getContents().toString());
    }

    public GeneralJournal initGeneralJournalHeadDataTasaheel(Date date) throws NumberFormatException, ParseException {

        GeneralJournal item = new GeneralJournal();

//        Cell serial = sheet.getCell(2, i);
//        Cell documentNumber = sheet.getCell(4, i);
        GlYear glYear = new GlYear();
        glYear.setId(73);
        item.setGlYear(glYear);
//        item.setSerial(Integer.parseInt(serial.getContents()));
//        item.setGeneralDecument(Integer.parseInt(documentNumber.getContents()));
        Symbol generalType = new Symbol();
        generalType.setId(679);
        item.setGeneralType(generalType);
        item.setGeneralData(date);

        item.setBranchId(getUserData().getUserBranch());
        item.setCreatedBy(getUserData().getUser());
        item.setCreationDate(new Date());
        item.setCompanyId(getUserData().getCompany());
        return item;
    }

    public String importGeneralJournalDetail(Sheet sheet, Map<Integer, GeneralJournal> itemMap) {
        GeneralJournalDetails item;
        StringBuilder msg = new StringBuilder();
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                item = new GeneralJournalDetails();

                Cell accountNumber = sheet.getCell(4, i);
                GlAccount account = glAccountService.getAccountNumber(Integer.parseInt(accountNumber.getContents()), getUserData().getUserBranch().getId());
                item.setGlACCOUNTId(account);
                if (account != null) {
                    initGeneralJournalDetailData(sheet, i, item, itemMap);
                    generaljournalDetailsService.addGenDetalils(item);
                } else {
                    msg.append(accountNumber.toString()).append("\n");
                }
            }
        }
        return msg.toString();
    }

    public void initGeneralJournalDetailData(Sheet sheet, int i, GeneralJournalDetails item, Map<Integer, GeneralJournal> itemMap) throws NumberFormatException {
        Cell documentNumber = sheet.getCell(1, i);
        Cell serial = sheet.getCell(3, i);

        Cell debit = sheet.getCell(5, i);
        Cell credit = sheet.getCell(6, i);
        Cell debitF = sheet.getCell(7, i);
        Cell creditF = sheet.getCell(8, i);
        Cell costCenterNumber = sheet.getCell(10, i);
        Cell adminUnitNumber = sheet.getCell(11, i);
        Cell remark = sheet.getCell(13, i);

        GeneralJournal parent = itemMap.get(Integer.parseInt(documentNumber.getContents()));
        item.setGeneralJournalId(parent);
        item.setSerial(Integer.parseInt(serial.getContents()));

        item.setDebitAmount(!credit.getContents().isEmpty() ? (new BigDecimal(credit.getContents())) : null);
        item.setDebitAmountLocal(!creditF.getContents().isEmpty() ? (new BigDecimal(creditF.getContents())) : null);
        item.setCreditamount(!debit.getContents().isEmpty() ? (new BigDecimal(debit.getContents())) : null);
        item.setCreditamountLocal(!debitF.getContents().isEmpty() ? (new BigDecimal(debitF.getContents())) : null);
        if (!costCenterNumber.getContents().isEmpty()) {
            CostCenter costCenter = costCenterService.findCostCenter(Integer.parseInt(costCenterNumber.getContents()));
            item.setCostCenterId(costCenter);
        }
        if (!adminUnitNumber.getContents().isEmpty()) {
            GlAdminUnit adminUnit = adminUnitService.findAdminUnit(Integer.parseInt(adminUnitNumber.getContents()));
            item.setAdminUnitId(adminUnit);
        }
        item.setDiscribtion(remark.getContents());
        item.setBranchId(getUserData().getUserBranch());
        item.setCreatedBy(getUserData().getUser());
        item.setCreationDate(new Date());
        item.setCompanyId(getUserData().getCompany());
    }

    // GeneralJournalHead Tasaheel
    public void importGeneralJournalHeadTasaheet(Sheet sheetHead) throws NumberFormatException, ParseException, IOException, Exception {
        GeneralJournalDTO item;
        GeneralJournalDTO generalJournal = new GeneralJournalDTO();
        List<GeneralJournalDetails> detailList = new ArrayList<>();
        String msg = null;
        Integer x = 0;
        Integer y = 0;
        Integer total = sheetHead.getRows() - 1;
        for (int i = 1; i < sheetHead.getRows(); i++) {
            progress = Float.valueOf((Integer.valueOf(i).floatValue() / total) * 100.0f).intValue();
            if (!sheetHead.getCell(0, i).getContents().isEmpty()) {
                if (!sheetHead.getCell(2, i).getContents().isEmpty()) {
                    if (x == 1 && y == 1) {
                        generaljournalDetailsService.addGenDetalils(detailList, null);
                        detailList = new ArrayList<>();
                    }
                    if (y == 1 || x == 0) {
                        item = new GeneralJournalDTO();
                        initGeneralJournalHeadDataTasaheet(sheetHead, i, item);
                        generalJournal = generaljournalServiceDTO.addGeneralJournalDTO(item, getUserData().getUser());
                        y = 0;
//                        detailList = new ArrayList<>();
                    }

                    msg = importGeneralJournalDetailTasaheet(sheetHead, i, generalJournal, detailList);
                    x = 1;
                } else {
                    y = 1;
                    msg = importGeneralJournalDetailTasaheet(sheetHead, i, generalJournal, detailList);
                }
            } else {
                if (x == 1 && y == 1) {
                    generaljournalDetailsService.addGenDetalils(detailList, null);
                    detailList = new ArrayList<>();
                }
            }
        }

        if (msg != null && !msg.isEmpty()) {
            OpenDlg(msg);
        } else {
            OpenDlg("sucesses");
        }

        System.out.println("تم الحفظ بنجاح");
    }

    public void initGeneralJournalHeadDataTasaheet(Sheet sheet, int i, GeneralJournalDTO item) throws NumberFormatException, ParseException {
        Cell dateTo = sheet.getCell(5, i);
        Cell remarkHead = sheet.getCell(7, i);

        GlYear glYear = new GlYear();
        glYear.setId(73);
        GlYearDTO glYearDTO = new GlYearDTO();
        glYearDTO.setId(glYear.getId());
        item.setGlYear(glYearDTO);
        SymbolDTO generalType = new SymbolDTO();
        generalType.setId(679);
        generalType.setSerial(1);
        item.setGeneralType(generalType);
//        item.setGeneralStatement(remark.getContents() + " " + remark2.getContents());
        if (!dateTo.getContents().isEmpty()) {
            Date datef1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateTo.getContents());
            item.setGeneralData(datef1);
        }
        item.setPost_flag(false);
        item.setGeneralStatement(remarkHead.getContents());
        item.setBranchId(getUserData().getUserBranch().getId());
        item.setCreatedBy(getUserData().getUser().getId());
        item.setCompanyId(getUserData().getCompany().getId());
    }

    public String importGeneralJournalDetailTasaheet(Sheet sheet, int i, GeneralJournalDTO generalJournal, List<GeneralJournalDetails> detailList) {
        GeneralJournalDetails item;
        StringBuilder msg = new StringBuilder();
        if (!sheet.getCell(0, i).getContents().isEmpty()) {
            item = new GeneralJournalDetails();

            Cell accountNumber = sheet.getCell(0, i);
            GlAccount account = glAccountService.getAccountNumber(Integer.parseInt(accountNumber.getContents()), getUserData().getUserBranch().getId());
            item.setGlACCOUNTId(account);
            if (account != null) {
                initGeneralJournalDetailDataTasaheet(sheet, i, item, generalJournal);
                detailList.add(item);
            } else {
                msg.append(accountNumber.toString()).append("\n");
            }
        }
        return msg.toString();
    }

    public void initGeneralJournalDetailDataTasaheet(Sheet sheet, int i, GeneralJournalDetails item, GeneralJournalDTO generalJournal) throws NumberFormatException {
        Cell debit = sheet.getCell(3, i);
        Cell credit = sheet.getCell(2, i);
        Cell debitF = sheet.getCell(3, i);
        Cell creditF = sheet.getCell(2, i);
        Cell remark = sheet.getCell(4, i);
        Cell serialExcel = sheet.getCell(6, i);
//        Cell remark2 = sheet.getCell(5, i);

        GeneralJournal generalJournal1 = new GeneralJournal();
        generalJournal1.setId(generalJournal.getId());
        item.setGeneralJournalId(generalJournal1);

        item.setDebitAmount(!credit.getContents().isEmpty() ? (new BigDecimal(credit.getContents())) : null);
        item.setDebitAmountLocal(!creditF.getContents().isEmpty() ? (new BigDecimal(creditF.getContents())) : null);
        item.setCreditamount(!debit.getContents().isEmpty() ? (new BigDecimal(debit.getContents())) : null);
        item.setCreditamountLocal(!debitF.getContents().isEmpty() ? (new BigDecimal(debitF.getContents())) : null);
        item.setDiscribtion(remark.getContents());
        item.setBranchId(getUserData().getUserBranch());
        item.setCreatedBy(getUserData().getUser());
        item.setCreationDate(new Date());
        item.setCompanyId(getUserData().getCompany());
        item.setSerialExcel(Integer.parseInt(serialExcel.getContents()));
    }

    // InvItem Code
    public void readInvItem() throws BiffException, IOException, Exception {
        setInputStreamUploadInvItem(getInvItemFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadInvItem());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        importInvItem(sheet);
        workbook.close();
    }

    public void importInvItem(Sheet sheet) throws NumberFormatException {
        InvItem invItem;
//        Map<String, Symbol> symbolMap = new ArrayMap<>();
//        Map<String, InvOrganizationSite> invOrganizationSiteMap = new ArrayMap<>();
//        Currency currency = new Currency();
//        List<Currency> currencyList = currencyService.getAllCurrencyByCompanyId(getUserData().getCompany().getId());
//        if (currencyList != null && !currencyList.isEmpty()) {
//            currency = currencyList.get(0);
//        } else {
//            System.out.print("not found currency");
//            return;
//        }

//        InventoryDelegator inventoryDelegator = new InventoryDelegator();
//        List<InventoryDelegator> delegatorList = invDelegatorService.getPurchaseByBranch(getUserData().getUserBranch().getId());
//        if (delegatorList != null && !delegatorList.isEmpty()) {
//            inventoryDelegator = delegatorList.get(0);
//        } else {
//            System.out.print("not found delegator");
//            return;
//        }

//        Symbol unitSymbol = new Symbol();
//        List<Symbol> unitList = symbolService.getUnitsByCompanyId(getUserData().getCompany().getId());
//        if (unitList != null && !unitList.isEmpty()) {
//            unitSymbol = unitList.get(0);
//        } else {
//            System.out.print("not found unit");
//            return;
//        }

        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                invItem = new InvItem();
//                initInvItemData(sheet, i, invItem, symbolMap, invOrganizationSiteMap, currency, inventoryDelegator, unitSymbol);
                initInvItemData(sheet, i, invItem, null, null, null, null, null);
                invItemService.addInvItem(invItem, null, null);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public void initInvItemData(Sheet sheet, int i, InvItem invItem, Map<String, Symbol> symbolMap, Map<String, InvOrganizationSite> invOrganizationSiteMap, Currency currency, InventoryDelegator inventoryDelegator, Symbol unitSymbol) throws NumberFormatException {
        Cell code = sheet.getCell(0, i);
        Cell name = sheet.getCell(1, i);
        Cell groupCode = sheet.getCell(2, i);
        Cell sellPrice = sheet.getCell(3, i);
        Cell costAvarage = sheet.getCell(4, i);
        Cell organizationSite = sheet.getCell(5, i);
        Cell unitCode = sheet.getCell(6, i);
        Cell name2 = sheet.getCell(7, i);
        Cell name3 = sheet.getCell(8, i);

//        String groupCodeString = groupCode.getContents();
//        if (groupCodeString != null && !groupCodeString.isEmpty()) {
//            InvGroup group = invGroupService.getInvGroupBySerial(getUserData().getUserBranch().getId(), Integer.parseInt(groupCodeString));
//            invItem.setGroupId(group);
//        }

//        String authorName = author.getContents();
//        Symbol Authorsymbol = new Symbol();
//        if (authorName != null && !authorName.isEmpty()) {
//            if (symbolMap.containsKey(authorName)) {
//                Authorsymbol = symbolMap.get(authorName);
//            } else {
//                Authorsymbol.setName(authorName);
//                Authorsymbol.setSerial(i + 1);
//                Authorsymbol.setCreatedBy(getUserData().getUser());
//                Authorsymbol.setCreationDate(new Date());
//                Authorsymbol.setCompanyId(getUserData().getCompany());
//                GeneralSymbol generalSymbol = new GeneralSymbol();
//                generalSymbol.setId(33);
//                Authorsymbol.setGeneralSymbolId(generalSymbol);
//                Authorsymbol = symbolService.addSymbol(Authorsymbol);
//                symbolMap.put(authorName, Authorsymbol);
//            }
//        } else {
//            Authorsymbol = null;
//        }
//        invItem.setAuthor(Authorsymbol);
//        String organizationSiteName = organizationSite.getContents();
//        InvOrganizationSite invOrganizationSite = organizationSiteService.getInvOrganizationSiteBySerial(getUserData().getUserBranch().getId(), organizationSiteName);

//        InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
//
//        if (organizationSiteName != null && !organizationSiteName.isEmpty()) {
//            if (invOrganizationSiteMap.containsKey(organizationSiteName)) {
//                invOrganizationSite = invOrganizationSiteMap.get(organizationSiteName);
//            } else {
//                invOrganizationSite.setCode((i + 1) + "");
//                invOrganizationSite.setName(organizationSiteName);
//                invOrganizationSite.setType(1);
//                invOrganizationSite.setCreatedBy(getUserData().getUser());
//                invOrganizationSite.setCreationDate(new Date());
//                invOrganizationSite.setCompanyId(getUserData().getCompany());
//                invOrganizationSite.setBranchId(getUserData().getUserBranch());
//                invOrganizationSite.setCurrencyId(currency);
//                invOrganizationSite.setDelegatorId(inventoryDelegator);
//                invOrganizationSite.setActive(1);
//                invOrganizationSite = organizationSiteService.addOrganizationSite(invOrganizationSite);
//                invOrganizationSiteMap.put(organizationSiteName, invOrganizationSite);
//            }
//        } else {
//            invOrganizationSite = null;
//        }
//        invItem.setAuthor(Authorsymbol);
//        String unitCodeString = unitCode.getContents();
//        if (unitCodeString != null && !unitCodeString.isEmpty()) {
//            SymbolDTO unitDto = symbolService.getSymbolByCompanyIdANDgeneralSymbolIdWithSerial(getUserData().getCompany().getId(), 3, Integer.parseInt(unitCodeString));
//            Symbol symbol = new Symbol();
//            symbol.setId(unitDto.getId());
//            invItem.setUnitId(symbol);
//        } else {
//            invItem.setUnitId(unitSymbol);
//        }

//        invItem.setSiteId(invOrganizationSite);

        invItem.setSellPrice(new BigDecimal(sellPrice.getContents()));
        invItem.setCostAverage(new BigDecimal(costAvarage.getContents()));
        invItem.setCode(code.getContents());
        invItem.setName(name.getContents() + " " + name2.getContents() + " "+name3.getContents());
        invItem.setBranchId(getUserData().getUserBranch());
        invItem.setIsInventoryItem(1);
        invItem.setCreatedBy(getUserData().getUser());
        invItem.setCreationDate(new Date());
        invItem.setCompanyId(getUserData().getCompany());
    }

    // inventory oppening balance Code
    public void readInvOpeiningBalance() throws BiffException, IOException, Exception {
        setInputStreamUploadinvOpeningBalance(getInvOpeningBalanceFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadinvOpeningBalance());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        importinvOpeningBalance(sheet);
        workbook.close();
    }

    public void importinvOpeningBalance(Sheet sheet) throws NumberFormatException, ParseException {
        InvOpenningBalanceItemDetail item;
        List<InvOpenningBalanceItem> invOpeningBalanceItemList = invOpeningBalanceItemService.getInvOpeningBalanceItemByBranchId(getUserData().getUserBranch().getId());
        if (invOpeningBalanceItemList != null && !invOpeningBalanceItemList.isEmpty()) {

            for (int i = 1; i < sheet.getRows(); i++) {
                if (!sheet.getCell(0, i).getContents().isEmpty()) {
                    item = new InvOpenningBalanceItemDetail();
                    initInvOpenningBalanceData(sheet, i, item, invOpeningBalanceItemList.get(0));
                    if (item.getItemId() != null && item.getItemId().getId() != null) {
                        invOpeningBalanceItemDetailService.addInvOpenningBalanceItemDetail(item);
                    }
                }
                System.out.println(i + "\n");
            }

            OpenDlg("sucesses");
            System.out.println("تم الحفظ بنجاح");
        } else {
            System.out.println("not found invOpeningBalance");
        }
    }

    public void initInvOpenningBalanceData(Sheet sheet, int i, InvOpenningBalanceItemDetail item, InvOpenningBalanceItem invOpeningBalanceItem) throws NumberFormatException, ParseException {
        InvItem itemId;
        Cell itemCode = sheet.getCell(0, i);
        Cell qty = sheet.getCell(1, i);
//        Cell price = sheet.getCell(2, i);
        List<InvItem> itemList = invItemService.findItemByCode(itemCode.getContents(), getUserData().getUserBranch().getId());
        if (itemList != null && !itemList.isEmpty()
                && qty.getContents() != null && !qty.getContents().isEmpty()) {
            itemId = itemList.get(0);
            item.setItemId(itemId);
            item.setQuantity(new BigDecimal(qty.getContents()));
            item.setCost(itemId.getCostAverage() == null ? BigDecimal.ZERO : itemId.getCostAverage());
            item.setNet(item.getCost().multiply(item.getQuantity()));
            item.setScrewing(BigDecimal.ONE);
            item.setSerial(i);
            item.setBalanceItemId(invOpeningBalanceItem);
            item.setBranchId(getUserData().getUserBranch());
            item.setCreatedBy(getUserData().getUser());
            item.setCreationDate(new Date());
            item.setCompanyId(getUserData().getCompany());
        }
    }

    // customer Code
    public void readCustomer() throws BiffException, IOException, Exception {
        disable = Boolean.FALSE;
        setInputStreamUploadCustomer(getCustomerFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadCustomer());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        importCustomer(sheet);
        workbook.close();
        disable = Boolean.TRUE;
    }

    public void importCustomer(Sheet sheet) throws NumberFormatException, ParseException {
        InvOrganizationSite item;

        Currency currency = new Currency();
        List<Currency> currencyList = currencyService.getAllCurrencyByCompanyId(getUserData().getCompany().getId());
        if (currencyList != null && !currencyList.isEmpty()) {
            currency = currencyList.get(0);
        } else {
            System.out.print("not found currency");
            return;
        }

        Map<String, String> mobileMap = organizationSiteService.findMobileOrganizationSite(getUserData().getUserBranch().getId());
        Map<String, String> mobileMapReversed = mobileMabReversed(mobileMap);
        StringBuilder error = new StringBuilder();
        Integer total = sheet.getRows() - 1;
        List<InvOrganizationSite> invOrganizationSiteList = new ArrayList<>();
        for (int i = 1; i < sheet.getRows(); i++) {
            progress = Float.valueOf((Integer.valueOf(i).floatValue() / total) * 100.0f).intValue();
            if (!sheet.getCell(1, i).getContents().isEmpty()) {
                item = new InvOrganizationSite();
                String msg = initCustomerData(sheet, i, item, currency, mobileMap, mobileMapReversed);
                if (msg != null && !msg.isEmpty()) {
                    error.append(msg);
                } else {
                    organizationSiteService.addOrganizationSite(item);
                    invOrganizationSiteList.add(item);
                }
            }
        }

        if (error != null && !error.toString().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", error.toString()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "تم ادخال البيانات الصحيحة برجاء تصحيح السطور الغير صحيحة واعادة ادخالها هي فقط مرة اخري"));
        } else {
            OpenDlg("sucesses");
            System.out.println("تم الحفظ بنجاح");
        }
    }

    private Map<String, String> mobileMabReversed(Map<String, String> mobileMab) {
        Map<String, String> reversedMobileMap = new HashMap<>();
        String reversed = null;
        for (Map.Entry<String, String> entry : mobileMab.entrySet()) {
            String key = entry.getKey();
            if (key != null && !key.isEmpty()) {
                reversed = new StringBuilder(key).reverse().toString();
                String mobileReversed = reversed.substring(0, 8);
                reversedMobileMap.put(mobileReversed, mobileReversed);
            }
        }
        return reversedMobileMap;
    }

    public synchronized String initCustomerData(Sheet sheet, int i, InvOrganizationSite item, Currency currency, Map<String, String> phoneMap, Map<String, String> mobileMapReversed) throws NumberFormatException, ParseException {
        StringBuilder error = new StringBuilder();
//        Cell code = sheet.getCell(0, i);
        Cell name = sheet.getCell(1, i);
        Cell mobile = sheet.getCell(2, i);
        Cell email = sheet.getCell(3, i);
        Cell job = sheet.getCell(4, i);
        Cell assignTo = sheet.getCell(5, i);
        Cell communicationSource = sheet.getCell(6, i);
        Cell phone = sheet.getCell(7, i);
        item.setActive(1);
//        item.setCode(code.getContents());
        item.setCurrencyId(currency);
        item.setName(name.getContents());
        item.setType(5);
        item.setBranchId(getUserData().getUserBranch());
        item.setCreatedBy(getUserData().getUser());
        item.setCreationDate(new Date());
        item.setCompanyId(getUserData().getCompany());
        item.setPhone(phone.getContents());
        item.setMobile(mobile.getContents());
        item.setEmail(email.getContents());

        List<Symbol> listStatus = symbolService.getSymbolBySerialByGeneralSymbol(getUserData().getCompany().getId(), 1, 57);
        item.setLeadStatus((listStatus == null || listStatus.isEmpty()) ? null : listStatus.get(0));

        if (name.getContents() == null || name.getContents().isEmpty()) {
            error.append("يجب ادخال اسم العميل فى السطر " + i + "<br/>");
        }
        if ((mobile.getContents() == null || mobile.getContents().isEmpty()) && (email.getContents() == null || email.getContents().isEmpty())) {
            error.append("يجب ادخال هاتف العميل أو ايميل العميل فى السطر " + i + "<br/>");
        }
        if (mobile.getContents() != null || !mobile.getContents().isEmpty()) {
            String custMobile = mobile.getContents();
            if (custMobile.length() >= 8) {
                String reversed = new StringBuilder(custMobile).reverse().toString();
                String custMobileReversed = reversed.substring(0, 8);
                if (phoneMap.containsKey(custMobile) || mobileMapReversed.containsKey(custMobileReversed)) {
                    error.append("هاتف العميل تم ادخاله من قبل فى السطر رقم " + i + "<br/>");
                } else {
                    phoneMap.put(custMobile, custMobile);
                    mobileMapReversed.put(custMobileReversed, custMobileReversed);
                }
            } else {
                error.append("هاتف العميل غير صحيح فى السطر رقم " + i + "<br/>");
            }

        }
        if (job.getContents() != null && !job.getContents().isEmpty()) {
            if (sheet.getCell(4, i).getType().equals(CellType.NUMBER)) {
                List<Symbol> list = symbolService.getSymbolBySerialByGeneralSymbol(getUserData().getCompany().getId(), Integer.parseInt(job.getContents()), 52);
                item.setWorkTypeId((list == null || list.isEmpty()) ? null : list.get(0));
            } else {
                error.append("يجب اختيار رقم وظيفه صحيحة فى السطر" + i);
            }
        }
        if (communicationSource.getContents() != null && !communicationSource.getContents().isEmpty()) {
            List<Symbol> list = symbolService.getSymbolBySerialByGeneralSymbol(getUserData().getCompany().getId(), Integer.parseInt(communicationSource.getContents()), 55);
            item.setLearnAboutCompanyfrom((list == null || list.isEmpty()) ? null : list.get(0));
        }
        if (assignTo.getContents() != null && !assignTo.getContents().isEmpty()) {
            List<Employee> list = employeeService.findEmployeeByCode(getUserData().getCompany().getId(), Integer.parseInt(assignTo.getContents()));
            item.setAssignedTo((list == null || list.isEmpty()) ? null : list.get(0));
        } else {
            error.append("يجب ادخال متابع فى السطر " + i);
        }

        return error.toString();
    }

    // customer Code Tasahel
    public void readCustomerTasahel() {
        try {
            setInputStreamUploadCustomer(getCustomerFile().getInputstream());

            Workbook workbook = null;
            workbook = Workbook.getWorkbook(getInputStreamUploadCustomer());
            Sheet sheet = workbook.getSheet(0);
            importCustomerTasahel(sheet);
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importCustomerTasahel(Sheet sheet) {
        InvOrganizationSite item;
        Map<String, Integer> delegatorMap = new HashMap<>();
        Map<Integer, Integer> workTypeMap = new HashMap<>();
        Map<Integer, Integer> regionMap = new HashMap<>();
        Map<Integer, Integer> LearnAboutCompanyfromMap = new HashMap<>();
        Currency currency = new Currency();

        Integer code = Integer.parseInt(organizationSiteService.findMaxCodeForOrganizationSiteByBranchId(getUserData().getUserBranch().getId(), 0));

        List<Currency> currencyList = currencyService.getAllCurrencyByCompanyId(getUserData().getCompany().getId());

        if (currencyList != null && !currencyList.isEmpty()) {
            currency = currencyList.get(0);
        } else {
            System.out.print("not found currency");
            return;
        }

        List<InventoryDelegator> inventoryDelegatorList = invDelegatorService.getSalesByBranch(getUserData().getUserBranch().getId());

        if (inventoryDelegatorList != null && !inventoryDelegatorList.isEmpty()) {
            for (InventoryDelegator delegator : inventoryDelegatorList) {
                delegatorMap.put(delegator.getCode(), delegator.getId());
            }
        } else {
            System.out.print("not found inventoryDelegator");
            return;
        }

        List<Symbol> workTypeList = symbolService.getSymbolByGeneralSymbol(getUserData().getCompany().getId(), 52);

        if (workTypeList != null && !workTypeList.isEmpty()) {
            for (Symbol symbol : workTypeList) {
                workTypeMap.put(symbol.getSerial(), symbol.getId());
            }
        } else {
            System.out.print("not found workType");
            return;
        }

        List<Symbol> regionList = symbolService.getSymbolByGeneralSymbol(getUserData().getCompany().getId(), 36);

        if (regionList != null && !regionList.isEmpty()) {
            for (Symbol symbol : regionList) {
                regionMap.put(symbol.getSerial(), symbol.getId());
            }
        } else {
            System.out.print("not found region");
            return;
        }

        List<Symbol> LearnAboutCompanyfromList = symbolService.getSymbolByGeneralSymbol(getUserData().getCompany().getId(), 55);

        if (LearnAboutCompanyfromList != null && !LearnAboutCompanyfromList.isEmpty()) {
            for (Symbol symbol : regionList) {
                LearnAboutCompanyfromMap.put(symbol.getSerial(), symbol.getId());
            }
        } else {
            System.out.print("not found currency");
            return;
        }

        for (int i = 2; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                item = new InvOrganizationSite();
                code = code + 1;
                initCustomerDataTasahel(sheet, i, item, currency, code, delegatorMap, workTypeMap, regionMap, LearnAboutCompanyfromMap);
                organizationSiteService.addOrganizationSite(item);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public void initCustomerDataTasahel(Sheet sheet, int i, InvOrganizationSite item, Currency currency, Integer code,
            Map<String, Integer> delegatorMap, Map<Integer, Integer> workTypeMap, Map<Integer, Integer> regionMap, Map<Integer, Integer> LearnAboutCompanyfromMap) {
        try {
            String delegatorCode = sheet.getCell(0, i).getContents().trim();
            String nationId = sheet.getCell(1, i).getContents().trim();
            String birthDate = sheet.getCell(2, i).getContents().trim();
            String name = sheet.getCell(3, i).getContents().trim();
            String mobile = sheet.getCell(4, i).getContents().trim();
            String workTypeSymbol = sheet.getCell(5, i).getContents().trim();
            String companyName = sheet.getCell(6, i).getContents().trim();
            String regionId = sheet.getCell(7, i).getContents().trim();
            String LearnAboutCompanyfrom = sheet.getCell(8, i).getContents().trim();
            String accountBankNumber = sheet.getCell(10, i).getContents().trim();
            String sponsorPhone1 = sheet.getCell(12, i).getContents().trim();
            String sponsorName1 = sheet.getCell(13, i).getContents().trim();
            String sponsorPhone2 = sheet.getCell(14, i).getContents().trim();
            String sponsorName2 = sheet.getCell(15, i).getContents().trim();

            item.setName(name);
            item.setCode(code.toString());
            item.setSponsorName1(sponsorName1);
            item.setSponsorName2(sponsorName2);
            item.setSponsorPhone1(sponsorPhone1);
            item.setSponsorPhone2(sponsorPhone2);
            item.setAccountBankNumber(accountBankNumber);
            item.setCompanyName(companyName);
            item.setMobile(mobile);
            item.setNationId(nationId);

            if (birthDate != null && !birthDate.isEmpty()) {
                String[] arr = birthDate.split("-");
//                item.setBirthdate(new SimpleDateFormat(arr[0] + "-" + arr[1] + "-" + arr[2]).parse(birthDate));
                item.setBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse(birthDate));
            }

            if (delegatorCode != null && !delegatorCode.isEmpty() && delegatorMap.get(delegatorCode) != null) {
                InventoryDelegator delegator = new InventoryDelegator();
                delegator.setId(delegatorMap.get(delegatorCode));
                item.setDelegatorId(delegator);
            }

            if (workTypeSymbol != null && !workTypeSymbol.isEmpty() && workTypeMap.get(Integer.parseInt(workTypeSymbol)) != null) {
                Symbol symbol = new Symbol();
                symbol.setId(workTypeMap.get(Integer.parseInt(workTypeSymbol)));
                item.setWorkTypeId(symbol);
            }

            if (regionId != null && !regionId.isEmpty() && regionMap.get(Integer.parseInt(regionId)) != null) {
                Symbol symbol = new Symbol();
                symbol.setId(regionMap.get(Integer.parseInt(regionId)));
                item.setRegionId(symbol);
            }

            if (LearnAboutCompanyfrom != null && !LearnAboutCompanyfrom.isEmpty() && LearnAboutCompanyfromMap.get(Integer.parseInt(LearnAboutCompanyfrom)) != null) {
                Symbol symbol = new Symbol();
                symbol.setId(LearnAboutCompanyfromMap.get(Integer.parseInt(LearnAboutCompanyfrom)));
                item.setLearnAboutCompanyfrom(symbol);
            }

            item.setActive(1);
            item.setCurrencyId(currency);
            item.setType(0);
            item.setBranchId(getUserData().getUserBranch());
            item.setCreatedBy(getUserData().getUser());
            item.setCreationDate(new Date());
            item.setCompanyId(getUserData().getCompany());
        } catch (ParseException ex) {
            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////BeginOfContractTasaheel///////////////////////////
    public void readContractTasahel() {
        try {
            setInputStreamUploadContract(getContractFile().getInputstream());

            Workbook workbook = null;
            workbook = Workbook.getWorkbook(getInputStreamUploadContract());
            Sheet sheet = workbook.getSheet(0);
            importContractTasahel(sheet);
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importContractTasahel(Sheet sheet) {
        InstContract item;
        Map<String, Integer> customerMap = new HashMap<>();

        List<InvOrganizationSite> organizationSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 0);

        if (organizationSiteList != null && !organizationSiteList.isEmpty()) {

            for (InvOrganizationSite organizationSite : organizationSiteList) {

                customerMap.put(organizationSite.getNationId(), organizationSite.getId());

            }

        } else {
            System.out.print("Customers Not Found");
            return;
        }
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                item = new InstContract();
                List<InstContractDetail> saveInstContractDetailList = new ArrayList<>();
                initContractDataTasahel(sheet, i, item, customerMap, saveInstContractDetailList);
                instContractService.addInstContractforTasaheel(item, saveInstContractDetailList);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public void initContractDataTasahel(Sheet sheet, int i, InstContract item,
            Map<String, Integer> customerMap, List<InstContractDetail> saveInstContractDetailList) {
        try {

            String nationId = sheet.getCell(0, i).getContents().trim();
            String startInstallment = sheet.getCell(1, i).getContents().trim();
            String date = sheet.getCell(2, i).getContents().trim();
            String installmentValue = sheet.getCell(3, i).getContents().trim();
            String installmentNumber = sheet.getCell(4, i).getContents().trim();
            String totalValue = sheet.getCell(5, i).getContents().trim();
            String fixedPrice = sheet.getCell(6, i).getContents().trim();
            String serial = sheet.getCell(7, i).getContents().trim();

            if (customerMap.get(nationId) != null) {
                InvOrganizationSite organizationSite = new InvOrganizationSite();
                organizationSite.setId(customerMap.get(nationId));
                item.setOrganizationSiteId(organizationSite);
            }
            item.setSerial(Integer.parseInt(serial));
            item.setStartInstallment(new SimpleDateFormat("dd/MM/yyyy").parse(startInstallment));
            item.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));
            item.setTotalValue(new BigDecimal(totalValue));
            item.setInstallmentNumber(Integer.parseInt(installmentNumber));
            item.setInstallmentValue(new BigDecimal(installmentValue));
            item.setDaysNumber(1);
            item.setAmount(BigDecimal.ONE);
            item.setDeliveryPayment(BigDecimal.ZERO);
            item.setAdvancePayment(BigDecimal.ZERO);
            item.setMurabahaRate((new BigDecimal(totalValue).subtract(new BigDecimal(installmentValue)).divide(new BigDecimal(installmentValue), 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))));
            item.setBranchId(getUserData().getUserBranch());
            item.setCreatedBy(getUserData().getUser());
            item.setCreationDate(new Date());
            item.setCompanyId(getUserData().getCompany());

//Adding Detail
            Calendar c = Calendar.getInstance();
            c.setTime(item.getStartInstallment());
            Date dueDate = item.getStartInstallment();
            for (int s = 0; s < item.getInstallmentNumber(); s++) {
                InstContractDetail instContractDetail = new InstContractDetail();
                instContractDetail.setValue(new BigDecimal(fixedPrice));
                instContractDetail.setDueDate(dueDate);
                instContractDetail.setSerial(s + 1);
                instContractDetail.setBranchId(getUserData().getUserBranch());
                instContractDetail.setCompanyId(getUserData().getUser().getCompanyId());
                instContractDetail.setCreationDate(new Date());
                instContractDetail.setCreatedBy(getUserData().getUser().getCreatedBy());
                saveInstContractDetailList.add(instContractDetail);
                c.add(Calendar.MONTH, item.getDaysNumber());
                dueDate = c.getTime();
            }
        } catch (ParseException ex) {

            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ////////////////////endOfContractTasaheel/////////////////////////////
    //////////////////////BeginOfCashTasaheel///////////////////////////
    public void readCashTasahel() {
        try {
            setInputStreamUploadContract(getContractFile().getInputstream());

            Workbook workbook = null;
            workbook = Workbook.getWorkbook(getInputStreamUploadContract());
            Sheet sheet = workbook.getSheet(0);
            importCashTasahel(sheet);
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importCashTasahel(Sheet sheet) {
        GlBankTransaction glBankTransaction;
        List<GlBankTransactionDetail> glBankTransactionDetailList;
        Map<Integer, List<InstContractDetail>> instContractDTOMap = new HashMap<>();
        Map<String, GlBank> glBankMap = new HashMap<>();

//        instContractDTOMap = instContractService.getALLInstContractByBranchIds(getUserData().getUserBranch().getId());// first modify
        List<GlBank> glBanks = glBankService.getAllGlBankByBranchId(getUserData().getUserBranch().getId());

        for (GlBank bank : glBanks) {
            glBankMap.put(bank.getCode(), bank);
        }

        Map<String, String> userDDs = getUserData().getUserDDs();

        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                glBankTransaction = new GlBankTransaction();
                glBankTransactionDetailList = new ArrayList<>();
                initCashDataTasahel(sheet, i, glBankTransaction, glBankTransactionDetailList, userDDs, instContractDTOMap, glBankMap);
                if (glBankTransaction.getValue() != null && glBankTransaction.getInstallmentId() != null && glBankTransaction.getDate() != null) {
                    glBankTransactionService.addGlBankTransactionForInstallment(glBankTransaction, glBankTransactionDetailList, null);
                }
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public void initCashDataTasahel(Sheet sheet, int i, GlBankTransaction glBankTransaction, List<GlBankTransactionDetail> glBankTransactionDetailList,
            Map<String, String> userDDs, Map<Integer, List<InstContractDetail>> instContractDTOMap, Map<String, GlBank> glBankMap) {
        try {

            String contractSerial = sheet.getCell(0, i).getContents().trim();
            String bankCode = sheet.getCell(1, i).getContents().trim();
            String value = sheet.getCell(2, i).getContents().trim();
            String date = sheet.getCell(3, i).getContents().trim();
            String remark = sheet.getCell(4, i).getContents().trim();

            if (value.isEmpty() || contractSerial.isEmpty() || date.isEmpty()) {
                return;
            }

            glBankTransaction.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch() : null);
            glBankTransaction.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);
            glBankTransaction.setCreatedBy(getUserData().getUser());
            glBankTransaction.setCreationDate(new Date());

            glBankTransaction.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));
            glBankTransaction.setPostFlag(0);
            glBankTransaction.setPaymentType(0);
            glBankTransaction.setRemark(remark);
            glBankTransaction.setOrganizationType(0);
            glBankTransaction.setTransactionType(1);
            glBankTransaction.setGlYear(getUserData().getGlYear().getYear());

            glBankTransaction.setValue(new BigDecimal(value));
            glBankTransaction.setValueLocal(new BigDecimal(value));
            glBankTransaction.setRate(BigDecimal.ONE);
            GlBank glBank = glBankMap.get(bankCode);
            if (glBank != null) {
                glBankTransaction.setGlBankId(glBank);
            } else {
                return;
            }
//            glBankTransaction.setGlBankId(null);

            List<InstContractDetail> contractDetails = instContractDTOMap.get(Integer.parseInt(contractSerial));

            if (contractDetails != null && !contractDetails.isEmpty()) {
                glBankTransaction.setInstallmentId(contractDetails.get(0));
            } else {
                return;
            }

//Adding Detail
            GlBankTransactionDetail bankTransactionDetail = new GlBankTransactionDetail();

            bankTransactionDetail.setCreatedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
            bankTransactionDetail.setCreationDate(new Date());

            bankTransactionDetail.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);

            bankTransactionDetail.setRemarks(remark);
            bankTransactionDetail.setValue(new BigDecimal(value));
            bankTransactionDetail.setValueLocal(new BigDecimal(value));

            bankTransactionDetail.setOrganizationSiteId(glBankTransaction.getInstallmentId().getInstContractId().getOrganizationSiteId());

            bankTransactionDetail.setAccountDebitId(glBankTransaction.getInstallmentId().getInstContractId().getOrganizationSiteId().getAccountId());
            bankTransactionDetail.setAccountCreditId(glBank.getAccountId());
            bankTransactionDetail.setBankIdFrom(glBank);
            bankTransactionDetail.setRateBankFrom(BigDecimal.ONE);

            glBankTransactionDetailList.add(bankTransactionDetail);

        } catch (ParseException ex) {

            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ////////////////////endOfCashTasaheel/////////////////////////////
    //////////////////////BeginOfInvPurchaseInvoice///////////////////////////
    public void readInvPurchaseInvoice() {
        try {
            setInputStreamUploadContract(getContractFile().getInputstream());

            Workbook workbook = null;
            workbook = Workbook.getWorkbook(getInputStreamUploadContract());
            Sheet sheet = workbook.getSheet(0);
            importInvPurchaseInvoice(sheet);
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importInvPurchaseInvoice(Sheet sheet) {
        InvPurchaseInvoice invPurchaseInvoice;

        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                invPurchaseInvoice = new InvPurchaseInvoice();
                initInvPurchaseInvoice(sheet, i, invPurchaseInvoice);
                invPurchaseInvoiceService.addInvPurchaseInvoiceTest(invPurchaseInvoice);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public void initInvPurchaseInvoice(Sheet sheet, int i, InvPurchaseInvoice invPurchaseInvoice) {
        try {

            String inventoryId = sheet.getCell(1, i).getContents().trim();
            String serial = sheet.getCell(2, i).getContents().trim();
            String payment_type = sheet.getCell(3, i).getContents().trim();
            String date = sheet.getCell(4, i).getContents().trim();
            String discount_type = sheet.getCell(7, i).getContents().trim();
            String discount = sheet.getCell(8, i).getContents().trim();
            String remarks = sheet.getCell(9, i).getContents().trim();

            invPurchaseInvoice.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch() : null);
            invPurchaseInvoice.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);
            invPurchaseInvoice.setCreatedBy(getUserData().getUser());
            invPurchaseInvoice.setCreationDate(new Date());

            invPurchaseInvoice.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));

            InvInventory inventory = new InvInventory();
            inventory.setId(Integer.parseInt(inventoryId));
            invPurchaseInvoice.setInvInventoryId(inventory);

            invPurchaseInvoice.setSerial(Integer.parseInt(serial) + 1000);
            invPurchaseInvoice.setPaymentType(Integer.parseInt(payment_type));
            invPurchaseInvoice.setDiscountType(Integer.parseInt(discount_type));
            invPurchaseInvoice.setDiscount(new BigDecimal(discount));
            invPurchaseInvoice.setRemarks(remarks);

        } catch (ParseException ex) {

            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////BeginOfInvPurchaseInvoice///////////////////////////
    public void readInvNotice() {
        try {
            setInputStreamUploadContract(getContractFile().getInputstream());

            Workbook workbook = null;
            workbook = Workbook.getWorkbook(getInputStreamUploadContract());
            Sheet sheet = workbook.getSheet(0);
            importInvNotice(sheet);
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importInvNotice(Sheet sheet) {
        Map<Integer, List<InstContractDetailDTO>> instContractDTOMap = new HashMap<>();
        instContractDTOMap = instContractService.getALLInstContractByBranchIds(getUserData().getUserBranch().getId());// first modify
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                InvNotice invNotice = new InvNotice();
                initInvNotice(sheet, i, invNotice, instContractDTOMap);
                invNotice = invNoticeService.addInvNotice(invNotice);
                if (invNotice.getMsg() != null && !invNotice.getMsg().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), invNotice.getMsg()));
                    return;
                }
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public void initInvNotice(Sheet sheet, int i, InvNotice invNotice, Map<Integer, List<InstContractDetailDTO>> instContractDTOMap) {
        try {

            String date = sheet.getCell(0, i).getContents().trim();
            String value = sheet.getCell(1, i).getContents().trim();
            String contrasaccountCode = sheet.getCell(2, i).getContents().trim();
            String remark = sheet.getCell(3, i).getContents().trim();
            String type = sheet.getCell(4, i).getContents().trim();
            String contractSerial = sheet.getCell(5, i).getContents().trim();
            String noticeNaming = sheet.getCell(6, i).getContents().trim();
            String serialExcel = sheet.getCell(7, i).getContents().trim();

            GlAccount glAccount = null;
            if (contrasaccountCode != null && !contrasaccountCode.isEmpty()) {
                glAccount = glAccountService.findGlAccountbyBranchIdAndAccountNumber(getUserData().getUserBranch().getId(), Integer.parseInt(contrasaccountCode));
            }
            List<InstContractDetailDTO> contractDetails = instContractDTOMap.get(Integer.parseInt(contractSerial));
            InstContract contractSerialFound = instContractService.findInstContractByBranchIdAndSerial(getUserData().getUserBranch().getId(), Integer.valueOf(contractSerial));
            if (contractSerialFound == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "رقم العقد غير موجود رقم السطر" + (i + 1)));
                return;
            }
            InstContractDetailDTO contractDetailDTOSelected = null;
            InstContractDetail contractDetail = null;
            if (contractDetails != null && !contractDetails.isEmpty()) {
                contractDetailDTOSelected = contractDetails.get(0);
                contractDetail = new InstContractDetail();
                contractDetail.setId(contractDetailDTOSelected.getId());
                invNotice.setInstallmentId(contractDetail);
            }
            Symbol symbol = null;
            if (noticeNaming != null && !noticeNaming.isEmpty()) {
                symbol = symbolService.getOneSymbolBySerial(getUserData().getCompany().getId(), Integer.parseInt(noticeNaming));
            }
            invNotice.setSerialExcel(Integer.parseInt(serialExcel));
            invNotice.setNoticeNaming(symbol);
            invNotice.setInstallmentId(contractDetail);
            invNotice.setContrastAccountId(glAccount);
            invNotice.setOrganizationSiteId(contractSerialFound.getOrganizationSiteId());
            invNotice.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));
            invNotice.setValue(new BigDecimal(value));
            invNotice.setRemark(remark);
            invNotice.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);
            invNotice.setCreatedBy(getUserData().getUser());
            invNotice.setCreationDate(new Date());
            invNotice.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch() : null);
            invNotice.setType(Integer.parseInt(type));

        } catch (ParseException ex) {

            Logger.getLogger(ImportMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ////////////////////endOfInvPurchaseInvoice/////////////////////////////
    public void transfer() {
        invPurchaseInvoiceService.findmofakroun();
    }

    public void updateCostAvarage() {
        invPurchaseInvoiceDetailsService.updateCostAvarage(getUserData().getUserBranch().getId());
    }

    public void excuteTransfer() {
        Currency curruncy = currencyService.getAllCurrencyByCompanyId(getUserData().getCompany().getId()).get(0);
        if (curruncy != null) {
            if (moduleId == 1) {
                List<GlAccount> accountListOld = glAccountService.findAllGlAccountsByBranchAndLevel(getBrachListSelectFrom().getId(), getLevel());
                for (GlAccount accountOld : accountListOld) {
                    accountOld.setId(null);
                    if (getBrachListSelectTo() != null && getBrachListSelectTo().getId() != null) {
                        Branch branch = new Branch();
                        branch.setId(getBrachListSelectTo().getId());
                        accountOld.setBranchId(branch);
                    }

                    if (getBrachListSelectTo() != null && getBrachListSelectTo().getCompanyId() != null) {
                        IsagCompany isagCompany = new IsagCompany();
                        isagCompany.setId(getBrachListSelectTo().getCompanyId());
                        accountOld.setCompanyId(isagCompany);
                    }

                    accountOld.setCurrencyId(curruncy);
                    if (accountOld.getParentAcc() != null) {
                        GlAccount accountparent = glAccountService.findGlAccountbyBranchIdAndAccountNumber(getBrachListSelectTo().getId(), accountOld.getParentAcc().getAccNumber());
                        accountOld.setParentAcc(accountparent);
                    }
                    glAccountService.addGlAccount(accountOld);
                }
            } else if (moduleId == 2) {
                List<RscWorkUnitsGuide> rscWorkUnitsGuides = rscWorkUnitsGuideService.getBranchRscWorkUnitsGuidesByBranchANDLevel(getBrachListSelectFrom().getId(), getLevel());
                for (RscWorkUnitsGuide rscWorkUnitsGuide : rscWorkUnitsGuides) {
                    rscWorkUnitsGuide.setId(null);
                    if (getBrachListSelectTo() != null && getBrachListSelectTo().getId() != null) {
                        Branch branch = new Branch();
                        branch.setId(getBrachListSelectTo().getId());
                        rscWorkUnitsGuide.setBranchId(branch);
                    }

                    if (getBrachListSelectTo() != null && getBrachListSelectTo().getCompanyId() != null) {
                        IsagCompany isagCompany = new IsagCompany();
                        isagCompany.setId(getBrachListSelectTo().getCompanyId());
                        rscWorkUnitsGuide.setCompanyId(isagCompany);
                    }

                    if (rscWorkUnitsGuide.getParent() != null) {
                        RscWorkUnitsGuide rscWorkUnitsGuideParent = rscWorkUnitsGuideService.findRscWorkUnitsGuidesbyBranchIdAndCode(getBrachListSelectTo().getId(), rscWorkUnitsGuide.getParent().getCode());
                        rscWorkUnitsGuide.setParent(rscWorkUnitsGuideParent);
                    }
                    rscWorkUnitsGuideService.addRscWorkUnitsGuideForTransfer(rscWorkUnitsGuide);
                }
            }
        } else {

        }
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the documentFile
     */
    public UploadedFile getDocumentFile() {
        return documentFile;
    }

    /**
     * @param documentFile the documentFile to set
     */
    public void setDocumentFile(UploadedFile documentFile) {
        this.documentFile = documentFile;
    }

    /**
     * @return the costCenterFile
     */
    public UploadedFile getCostCenterFile() {
        return costCenterFile;
    }

    /**
     * @param costCenterFile the costCenterFile to set
     */
    public void setCostCenterFile(UploadedFile costCenterFile) {
        this.costCenterFile = costCenterFile;
    }

    /**
     * @return the adminUnitFile
     */
    public UploadedFile getAdminUnitFile() {
        return adminUnitFile;
    }

    /**
     * @param adminUnitFile the adminUnitFile to set
     */
    public void setAdminUnitFile(UploadedFile adminUnitFile) {
        this.adminUnitFile = adminUnitFile;
    }

    /**
     * @return the yearFile
     */
    public UploadedFile getYearFile() {
        return yearFile;
    }

    /**
     * @param yearFile the yearFile to set
     */
    public void setYearFile(UploadedFile yearFile) {
        this.yearFile = yearFile;
    }

    /**
     * @return the glAccountFile
     */
    public UploadedFile getGlAccountFile() {
        return glAccountFile;
    }

    /**
     * @param glAccountFile the glAccountFile to set
     */
    public void setGlAccountFile(UploadedFile glAccountFile) {
        this.glAccountFile = glAccountFile;
    }

    /**
     * @return the GeneralJournalHeadFile
     */
    public UploadedFile getGeneralJournalHeadFile() {
        return GeneralJournalHeadFile;
    }

    /**
     * @param GeneralJournalHeadFile the GeneralJournalHeadFile to set
     */
    public void setGeneralJournalHeadFile(UploadedFile GeneralJournalHeadFile) {
        this.GeneralJournalHeadFile = GeneralJournalHeadFile;
    }

    /**
     * @return the inputStreamUploadDocument
     */
    public InputStream getInputStreamUploadDocument() {
        return inputStreamUploadDocument;
    }

    /**
     * @param inputStreamUploadDocument the inputStreamUploadDocument to set
     */
    public void setInputStreamUploadDocument(InputStream inputStreamUploadDocument) {
        this.inputStreamUploadDocument = inputStreamUploadDocument;
    }

    /**
     * @return the inputStreamUploadCostCenter
     */
    public InputStream getInputStreamUploadCostCenter() {
        return inputStreamUploadCostCenter;
    }

    /**
     * @param inputStreamUploadCostCenter the inputStreamUploadCostCenter to set
     */
    public void setInputStreamUploadCostCenter(InputStream inputStreamUploadCostCenter) {
        this.inputStreamUploadCostCenter = inputStreamUploadCostCenter;
    }

    /**
     * @return the inputStreamUploadAdminUnit
     */
    public InputStream getInputStreamUploadAdminUnit() {
        return inputStreamUploadAdminUnit;
    }

    /**
     * @param inputStreamUploadAdminUnit the inputStreamUploadAdminUnit to set
     */
    public void setInputStreamUploadAdminUnit(InputStream inputStreamUploadAdminUnit) {
        this.inputStreamUploadAdminUnit = inputStreamUploadAdminUnit;
    }

    /**
     * @return the inputStreamUploadYear
     */
    public InputStream getInputStreamUploadYear() {
        return inputStreamUploadYear;
    }

    /**
     * @param inputStreamUploadYear the inputStreamUploadYear to set
     */
    public void setInputStreamUploadYear(InputStream inputStreamUploadYear) {
        this.inputStreamUploadYear = inputStreamUploadYear;
    }

    /**
     * @return the inputStreamUploadGlAccount
     */
    public InputStream getInputStreamUploadGlAccount() {
        return inputStreamUploadGlAccount;
    }

    /**
     * @param inputStreamUploadGlAccount the inputStreamUploadGlAccount to set
     */
    public void setInputStreamUploadGlAccount(InputStream inputStreamUploadGlAccount) {
        this.inputStreamUploadGlAccount = inputStreamUploadGlAccount;
    }

    /**
     * @return the inputStreamUploadGeneralJournalHead
     */
    public InputStream getInputStreamUploadGeneralJournalHead() {
        return inputStreamUploadGeneralJournalHead;
    }

    /**
     * @param inputStreamUploadGeneralJournalHead the
     * inputStreamUploadGeneralJournalHead to set
     */
    public void setInputStreamUploadGeneralJournalHead(InputStream inputStreamUploadGeneralJournalHead) {
        this.inputStreamUploadGeneralJournalHead = inputStreamUploadGeneralJournalHead;
    }

    /**
     * @return the currencyFile
     */
    public UploadedFile getCurrencyFile() {
        return currencyFile;
    }

    /**
     * @param currencyFile the currencyFile to set
     */
    public void setCurrencyFile(UploadedFile currencyFile) {
        this.currencyFile = currencyFile;
    }

    /**
     * @return the inputStreamUploadCurrency
     */
    public InputStream getInputStreamUploadCurrency() {
        return inputStreamUploadCurrency;
    }

    /**
     * @param inputStreamUploadCurrency the inputStreamUploadCurrency to set
     */
    public void setInputStreamUploadCurrency(InputStream inputStreamUploadCurrency) {
        this.inputStreamUploadCurrency = inputStreamUploadCurrency;
    }

    /**
     * @return the invItemFile
     */
    public UploadedFile getInvItemFile() {
        return invItemFile;
    }

    /**
     * @param invItemFile the invItemFile to set
     */
    public void setInvItemFile(UploadedFile invItemFile) {
        this.invItemFile = invItemFile;
    }

    /**
     * @return the inputStreamUploadInvItem
     */
    public InputStream getInputStreamUploadInvItem() {
        return inputStreamUploadInvItem;
    }

    /**
     * @param inputStreamUploadInvItem the inputStreamUploadInvItem to set
     */
    public void setInputStreamUploadInvItem(InputStream inputStreamUploadInvItem) {
        this.inputStreamUploadInvItem = inputStreamUploadInvItem;
    }

    /**
     * @return the inputStreamUploadinvOpeningBalance
     */
    public InputStream getInputStreamUploadinvOpeningBalance() {
        return inputStreamUploadinvOpeningBalance;
    }

    /**
     * @param inputStreamUploadinvOpeningBalance the
     * inputStreamUploadinvOpeningBalance to set
     */
    public void setInputStreamUploadinvOpeningBalance(InputStream inputStreamUploadinvOpeningBalance) {
        this.inputStreamUploadinvOpeningBalance = inputStreamUploadinvOpeningBalance;
    }

    /**
     * @return the invOpeningBalanceFile
     */
    public UploadedFile getInvOpeningBalanceFile() {
        return invOpeningBalanceFile;
    }

    /**
     * @param invOpeningBalanceFile the invOpeningBalanceFile to set
     */
    public void setInvOpeningBalanceFile(UploadedFile invOpeningBalanceFile) {
        this.invOpeningBalanceFile = invOpeningBalanceFile;
    }

    /**
     * @return the customerFile
     */
    public UploadedFile getCustomerFile() {
        return customerFile;
    }

    /**
     * @param customerFile the customerFile to set
     */
    public void setCustomerFile(UploadedFile customerFile) {
        this.customerFile = customerFile;
    }

    /**
     * @return the inputStreamUploadCustomer
     */
    public InputStream getInputStreamUploadCustomer() {
        return inputStreamUploadCustomer;
    }

    /**
     * @param inputStreamUploadCustomer the inputStreamUploadCustomer to set
     */
    public void setInputStreamUploadCustomer(InputStream inputStreamUploadCustomer) {
        this.inputStreamUploadCustomer = inputStreamUploadCustomer;
    }

    /**
     * @return the instContractService
     */
    public InstContractService getInstContractService() {
        return instContractService;
    }

    /**
     * @param instContractService the instContractService to set
     */
    public void setInstContractService(InstContractService instContractService) {
        this.instContractService = instContractService;
    }

    /**
     * @return the ContractFile
     */
    public UploadedFile getContractFile() {
        return ContractFile;
    }

    /**
     * @param ContractFile the ContractFile to set
     */
    public void setContractFile(UploadedFile ContractFile) {
        this.ContractFile = ContractFile;
    }

    /**
     * @return the inputStreamUploadContract
     */
    public InputStream getInputStreamUploadContract() {
        return inputStreamUploadContract;
    }

    /**
     * @param inputStreamUploadContract the inputStreamUploadContract to set
     */
    public void setInputStreamUploadContract(InputStream inputStreamUploadContract) {
        this.inputStreamUploadContract = inputStreamUploadContract;
    }

    /**
     * @return the contractDetailsService
     */
    public InstContractDetailsService getContractDetailsService() {
        return contractDetailsService;
    }

    /**
     * @param contractDetailsService the contractDetailsService to set
     */
    public void setContractDetailsService(InstContractDetailsService contractDetailsService) {
        this.contractDetailsService = contractDetailsService;
    }

    /**
     * @return the glBankService
     */
    public GlBankService getGlBankService() {
        return glBankService;
    }

    /**
     * @param glBankService the glBankService to set
     */
    public void setGlBankService(GlBankService glBankService) {
        this.glBankService = glBankService;
    }

    /**
     * @return the brachListFrom
     */
    public List<APIBranchDTO> getBrachListFrom() {
        if (brachListFrom == null || brachListFrom.isEmpty()) {
            brachListFrom = branchService.getAllBranchDTO();
            setBranchConverterFrom(new APIBranchConverter(brachListFrom));
        }
        return brachListFrom;
    }

    /**
     * @param brachListFrom the brachListFrom to set
     */
    public void setBrachListFrom(List<APIBranchDTO> brachListFrom) {
        this.brachListFrom = brachListFrom;
    }

    /**
     * @return the brachListTo
     */
    public List<APIBranchDTO> getBrachListTo() {
        if (brachListTo == null || brachListTo.isEmpty()) {
            brachListTo = branchService.getAllBranchDTO();
            setBranchConverterTo(new APIBranchConverter(brachListTo));
        }
        return brachListTo;
    }

    /**
     * @param brachListTo the brachListTo to set
     */
    public void setBrachListTo(List<APIBranchDTO> brachListTo) {
        this.brachListTo = brachListTo;
    }

    /**
     * @return the moduleId
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * @param moduleId the moduleId to set
     */
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * @return the branchConverterFrom
     */
    public APIBranchConverter getBranchConverterFrom() {
        return branchConverterFrom;
    }

    /**
     * @param branchConverterFrom the branchConverterFrom to set
     */
    public void setBranchConverterFrom(APIBranchConverter branchConverterFrom) {
        this.branchConverterFrom = branchConverterFrom;
    }

    /**
     * @return the branchConverterTo
     */
    public APIBranchConverter getBranchConverterTo() {
        return branchConverterTo;
    }

    /**
     * @param branchConverterTo the branchConverterTo to set
     */
    public void setBranchConverterTo(APIBranchConverter branchConverterTo) {
        this.branchConverterTo = branchConverterTo;
    }

    /**
     * @return the brachListSelectFrom
     */
    public APIBranchDTO getBrachListSelectFrom() {
        return brachListSelectFrom;
    }

    /**
     * @param brachListSelectFrom the brachListSelectFrom to set
     */
    public void setBrachListSelectFrom(APIBranchDTO brachListSelectFrom) {
        this.brachListSelectFrom = brachListSelectFrom;
    }

    /**
     * @return the brachListSelectTo
     */
    public APIBranchDTO getBrachListSelectTo() {
        return brachListSelectTo;
    }

    /**
     * @param brachListSelectTo the brachListSelectTo to set
     */
    public void setBrachListSelectTo(APIBranchDTO brachListSelectTo) {
        this.brachListSelectTo = brachListSelectTo;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the progress
     */
    public Integer getProgress() {
        if (progress == null) {
            progress = 0;
        }
        return progress;
    }

    /**
     * @param progress the progress to set
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    /**
     * @return the disable
     */
    public Boolean getDisable() {
        if (disable == null) {
            disable = Boolean.FALSE;
        }
        return disable;
    }

    /**
     * @param disable the disable to set
     */
    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

}
