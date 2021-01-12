/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package importData;

import com.toby.businessservice.BranchService;
import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.CurrencyService;
import com.toby.businessservice.GeneraljournalDetailsService;
import com.toby.businessservice.GeneraljournalService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlAdminUnitService;
import com.toby.businessservice.GlBankService;
import com.toby.businessservice.GlBankTransactionService;
import com.toby.businessservice.GlYearService;
import com.toby.businessservice.InvDelegatorService;
import com.toby.businessservice.InvItemService;
import com.toby.businessservice.InvOpeningBalanceItemDetailService;
import com.toby.businessservice.InvOpeningBalanceItemService;
import com.toby.businessservice.InvPurchaseInvoiceDetailsService;
import com.toby.businessservice.InvPurchaseInvoiceService;
import com.toby.businessservice.InvTransferService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.ProItemProductionStagesService;
import com.toby.businessservice.SymbolService;
import com.toby.businessservice.TobyUserService;
import com.toby.converter.BranchConverter;
import com.toby.define.AccountClassEnum;
import com.toby.define.AdminUnitDependantEnum;
import com.toby.define.AssisstantAccountDependantEnum;
import com.toby.define.CostCenterDependantEnum;
import com.toby.entity.Branch;
import com.toby.entity.Currency;
import com.toby.entity.GlAccount;
import com.toby.entity.InvItem;
import com.toby.entity.InvOpenningBalanceItem;
import com.toby.entity.InvOpenningBalanceItemDetail;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InventoryDelegator;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.toby.Basic;
import com.toby.toby.UserData;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jxl.Cell;
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

    private List<Branch> brachListFrom;
    private List<Branch> brachListTo;

    private Branch brachListSelectFrom;
    private Branch brachListSelectTo;

    private BranchConverter branchConverterFrom;
    private BranchConverter branchConverterTo;

    private Integer moduleId = 1;
    private Integer level;

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
    private GlBankTransactionService glBankTransactionService;
    @EJB
    private GlBankService glBankService;
    @EJB
    private BranchService branchService;
    @EJB
    private TobyUserService tobyUserService;
    @EJB
    private ProItemProductionStagesService itemProductionStagesService;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
    }

    public void OpenDlg(String dlgvar) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('" + dlgvar + "').show();");
    }

    // Update InvItem 
    public void updateEmployee() throws BiffException, IOException, Exception {
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
//        importUpdateEmployee(sheet);
        importUpdateInvItem3(sheet);
        workbook.close();
    }

    public void importUpdateEmployee(Sheet sheet) throws NumberFormatException {
        List<TobyUser> userList = new ArrayList<>();
        TobyUser user = new TobyUser();
        for (int i = 4; i < sheet.getRows(); i++) {
            if (!sheet.getCell(1, i).getContents().isEmpty()) {
                Cell employeeCode = sheet.getCell(1, i);
                Cell employeeNameAr = sheet.getCell(2, i);
                Cell employeeJob = sheet.getCell(5, i);
                Cell unitPrice = sheet.getCell(7, i);
                user = new TobyUser();
                user.setName(employeeNameAr.getContents());
                user.setPassword("123456");
                user.setUserCode("tbt" + employeeCode.getContents());
                Symbol sym = new Symbol();
                sym.setId(26);
                user.setLang(sym);
                user.setMaster(0);
                TobyCompany companySelected = new TobyCompany();
                companySelected.setId(125);
                user.setCompanyId(companySelected);
                user.setCreationDate(new Date());
                user.setCreatedBy(userData.getUser());
                Branch branch = new Branch();
                branch.setId(182);
                user.setBranchId(branch);

                user.setMsg(employeeJob.getContents());
                user.setSallary(new BigDecimal(unitPrice.getContents()));
                userList.add(user);
            }

        }

        tobyUserService.updateInvItem2(userList);
        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    // Update InvItem 
    public void updateInvItem() throws BiffException, IOException, Exception {
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
        importUpdateInvItem(sheet);
        workbook.close();
    }

    public void importUpdateInvItem(Sheet sheet) throws NumberFormatException {
        InvItem invItem;
        Map<String, InvItem> invItemMap = new ArrayMap<>();

        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {

                Cell suplierName = sheet.getCell(7, i);
                Cell storeName = sheet.getCell(8, i);
                Cell itemName = sheet.getCell(9, i);
                Cell itemCode = sheet.getCell(10, i);

                invItem = new InvItem();

                if (itemName != null && !itemName.getContents().trim().isEmpty() && itemCode != null && !itemCode.getContents().trim().isEmpty()) {
                    invItem.setNameen(suplierName.getContents());
                    invItem.setImage(storeName.getContents());
                    invItemMap.put(itemCode.getContents().trim(), invItem);
                }

            }
        }

        invItemService.updateInvItem2(invItemMap, getUserData().getUserlogin());

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    // Update InvItem 
    public void updateInvItem3() throws BiffException, IOException, Exception {
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
        importUpdateInvItem3(sheet);
        workbook.close();
    }

    public void importUpdateInvItem3(Sheet sheet) throws NumberFormatException {
        InvItem invItem;
        Map<String, InvItem> invItemMap = new ArrayMap<>();

        for (int i = 0; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {

                Cell itemCode = sheet.getCell(0, i);

                invItem = new InvItem();

                if (itemCode != null && !itemCode.getContents().trim().isEmpty()) {
                    invItemMap.put(itemCode.getContents().trim(), invItem);
                }

            }
        }

        itemProductionStagesService.saveAll(invItemMap, getUserData().getUserlogin());

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
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
        Map<String, Symbol> symbolMap = new ArrayMap<>();
        Map<String, InvOrganizationSite> invOrganizationSiteMap = new ArrayMap<>();
        Currency currency = new Currency();
        List<Currency> currencyList = currencyService.getAllCurrencyByCompanyId(getUserData().getCompany().getId());
        if (currencyList != null && !currencyList.isEmpty()) {
            currency = currencyList.get(0);
        } else {
            System.out.print("not found currency");
            return;
        }

        InventoryDelegator inventoryDelegator = new InventoryDelegator();
        List<InventoryDelegator> delegatorList = invDelegatorService.getPurchaseByBranch(getUserData().getUserBranch().getId());
        if (delegatorList != null && !delegatorList.isEmpty()) {
            inventoryDelegator = delegatorList.get(0);
        } else {
            System.out.print("not found delegator");
            return;
        }

        Symbol unitSymbol = new Symbol();
        List<Symbol> unitList = symbolService.getUnitsByCompanyId(getUserData().getCompany().getId());
        if (unitList != null && !unitList.isEmpty()) {
            unitSymbol = unitList.get(0);
        } else {
            System.out.print("not found unit");
            return;
        }

        Map<String, String> mapItems = invItemService.findInvItemDTOMap(getUserData().getUserBranch().getId());

        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                String code = sheet.getCell(8, i).getContents();
                if (!mapItems.containsKey(code)) {
                    invItem = new InvItem();
                    initInvItemData(sheet, i, invItem, symbolMap, invOrganizationSiteMap, currency, inventoryDelegator, unitSymbol);
                    invItemService.addInvItem(invItem, null, null);
                }
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public void initInvItemData(Sheet sheet, int i, InvItem invItem, Map<String, Symbol> symbolMap, Map<String, InvOrganizationSite> invOrganizationSiteMap, Currency currency, InventoryDelegator inventoryDelegator, Symbol unitSymbol) throws NumberFormatException {

        Cell meters = sheet.getCell(0, i);
        Cell minpriceyoung = sheet.getCell(1, i);
        Cell maxpriceyoung = sheet.getCell(2, i);
        Cell minpricemen = sheet.getCell(3, i);
        Cell maxpricemen = sheet.getCell(4, i);

        Cell type = sheet.getCell(5, i);
        Cell sellPrice = sheet.getCell(6, i);

        Cell name = sheet.getCell(7, i);
        Cell code = sheet.getCell(8, i);

        System.out.println("Line -> " + i + " Code -> " + code.getContents().toString() + " Name -> " + name.getContents().toString());

        if (type.getContents() != null && type.getContents().equals("عرضين")) {
            invItem.setItemNatural(2);
        } else if (type.getContents() != null && type.getContents().equals("عرض واحد")) {
            invItem.setItemNatural(1);
        }
        invItem.setNumbermetersyoung(new BigDecimal(meters.getContents()));
        invItem.setNumbermetersmen(new BigDecimal(meters.getContents()));
        invItem.setNumbermetersfreeyoung(new BigDecimal(meters.getContents()));
        invItem.setNumbermetersfreemen(new BigDecimal(meters.getContents()));

        invItem.setMinpriceyoung(new BigDecimal(minpriceyoung.getContents()));
        invItem.setMaxpriceyoung(new BigDecimal(maxpriceyoung.getContents()));
        invItem.setMinpricemen(new BigDecimal(minpricemen.getContents()));
        invItem.setMaxpricemen(new BigDecimal(maxpricemen.getContents()));
        invItem.setIsNew(1);

        invItem.setUnitId(unitSymbol);
        invItem.setSellPrice(new BigDecimal(sellPrice.getContents()));
        invItem.setCode(code.getContents());
        invItem.setName(name.getContents());
        invItem.setBranchId(getUserData().getUserBranch());
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
            Map<String, String> listItem = invItemService.findInvItemDTOMapForNewItem(getUserData().getUserBranch().getId());
            for (int i = 1; i < sheet.getRows(); i++) {
                if (!sheet.getCell(0, i).getContents().isEmpty()) {
                    String itemCode = sheet.getCell(1, i).getContents();
                    if (listItem.containsKey(itemCode)) {
                        item = new InvOpenningBalanceItemDetail();
                        initInvOpenningBalanceData(sheet, i, item, invOpeningBalanceItemList.get(0));
                        if (item.getItemId() != null && item.getItemId().getId() != null && item.getItemId().getIsInventoryItem() == 1) {
                            invOpeningBalanceItemDetailService.addInvOpenningBalanceItemDetail(item);
                        }
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
        Cell itemCode = sheet.getCell(1, i);
        Cell qty = sheet.getCell(0, i);
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

        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                item = new InvOrganizationSite();
                initCustomerData(sheet, i, item, currency);
                organizationSiteService.addOrganizationSite(item);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public void initCustomerData(Sheet sheet, int i, InvOrganizationSite item, Currency currency) throws NumberFormatException, ParseException {
        Cell itemCode = sheet.getCell(0, i);
        Cell itemName = sheet.getCell(1, i);
        Cell mobile = sheet.getCell(2, i);
        item.setActive(1);
        item.setMobile(mobile.getContents());
        item.setCode(itemCode.getContents());
        item.setCurrencyId(currency);
        item.setName(itemName.getContents());
        item.setType(0);
        item.setBranchId(getUserData().getUserBranch());
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
        Currency currency = new Currency();
        List<Currency> currencyList = currencyService.getAllCurrencyByCompanyId(getUserData().getCompany().getId());
        if (currencyList != null && !currencyList.isEmpty()) {
            currency = currencyList.get(0);
        } else {
            System.out.print("not found currency");
            return;
        }
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                item = new GlAccount();
                Cell parent = initGlAccountData(sheet, i, item, itemMap, currency);
                glAccountParent(parent, itemMap, item, parentCode);

                glAccountService.addGlAccount(item);
            }
        }

        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    public Cell initGlAccountData(Sheet sheet, int i, GlAccount item, Map<Integer, GlAccount> itemMap, Currency currency) throws NumberFormatException {
        Cell remarks = sheet.getCell(0, i);
        Cell accClass = sheet.getCell(1, i);
        Cell levalAcc = sheet.getCell(2, i);
        Cell accNumber = sheet.getCell(3, i);
        Cell parent = sheet.getCell(4, i);
        Cell name = sheet.getCell(5, i);
        Cell shotCode = sheet.getCell(6, i);

        item.setRemarks(remarks.getContents());
        item.setAccNumber(Integer.parseInt(!accNumber.getContents().isEmpty() ? accNumber.getContents() : null));
        item.setName(!name.getContents().isEmpty() ? name.getContents() : "");
        item.setLevelAcc(!levalAcc.getContents().isEmpty() ? Integer.parseInt(levalAcc.getContents()) : null);
        if (!levalAcc.getContents().isEmpty()) {
            if ("9".equals(levalAcc.getContents())) {
                item.setType(0);
            } else {
                item.setType(1);
            }
        }
        item.setShotCode(!shotCode.getContents().isEmpty() ? Integer.parseInt(shotCode.getContents()) : null);
//        if (!accGroup.getContents().isEmpty()) {
//            if ("1".equals(accGroup.getContents())) {
//                item.setAccGroup(AccountGroupEnum.ASSETS);
//            } else if ("2".equals(accGroup.getContents())) {
//                item.setAccGroup(AccountGroupEnum.DEDUCTION);
//            } else if ("3".equals(accGroup.getContents())) {
//                item.setAccGroup(AccountGroupEnum.EXPENSES);
//            } else if ("4".equals(accGroup.getContents())) {
//                item.setAccGroup(AccountGroupEnum.INCOME);
//            }
//        }
        item.setStatus(Boolean.TRUE);
        item.setCostCenter(CostCenterDependantEnum.CC_OPTIONAL);
        item.setAdministrativeUnit(AdminUnitDependantEnum.ADMUNT_OPTIONAL);
        item.setAssistantAcc(AssisstantAccountDependantEnum.ASSACC_OPTIONAL);
        if (!accClass.getContents().isEmpty()) {
            if ("مدين".equals(accClass.getContents())) {
                item.setAccClass(AccountClassEnum.DEBIT);
            } else if ("دائن".equals(accClass.getContents())) {
                item.setAccClass(AccountClassEnum.CREDIT);
            }
        }
        item.setBranchId(getUserData().getUserBranch());
        item.setCreatedBy(getUserData().getUser());
        item.setCreationDate(new Date());
        item.setCompanyId(getUserData().getCompany());
        item.setCurrencyId(currency);

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
            }

            if (itemParent != null) {
                item.setParentAcc(itemParent);
                itemMap.put(itemParent.getAccNumber(), itemParent);
            }
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
    public List<Branch> getBrachListFrom() {
        if (brachListFrom == null || brachListFrom.isEmpty()) {
            brachListFrom = branchService.getAllBranch();
            setBranchConverterFrom(new BranchConverter(brachListFrom));
        }
        return brachListFrom;
    }

    /**
     * @param brachListFrom the brachListFrom to set
     */
    public void setBrachListFrom(List<Branch> brachListFrom) {
        this.brachListFrom = brachListFrom;
    }

    /**
     * @return the brachListTo
     */
    public List<Branch> getBrachListTo() {
        if (brachListTo == null || brachListTo.isEmpty()) {
            brachListTo = branchService.getAllBranch();
            setBranchConverterTo(new BranchConverter(brachListTo));
        }
        return brachListTo;
    }

    /**
     * @param brachListTo the brachListTo to set
     */
    public void setBrachListTo(List<Branch> brachListTo) {
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
    public BranchConverter getBranchConverterFrom() {
        return branchConverterFrom;
    }

    /**
     * @param branchConverterFrom the branchConverterFrom to set
     */
    public void setBranchConverterFrom(BranchConverter branchConverterFrom) {
        this.branchConverterFrom = branchConverterFrom;
    }

    /**
     * @return the branchConverterTo
     */
    public BranchConverter getBranchConverterTo() {
        return branchConverterTo;
    }

    /**
     * @param branchConverterTo the branchConverterTo to set
     */
    public void setBranchConverterTo(BranchConverter branchConverterTo) {
        this.branchConverterTo = branchConverterTo;
    }

    /**
     * @return the brachListSelectFrom
     */
    public Branch getBrachListSelectFrom() {
        return brachListSelectFrom;
    }

    /**
     * @param brachListSelectFrom the brachListSelectFrom to set
     */
    public void setBrachListSelectFrom(Branch brachListSelectFrom) {
        this.brachListSelectFrom = brachListSelectFrom;
    }

    /**
     * @return the brachListSelectTo
     */
    public Branch getBrachListSelectTo() {
        return brachListSelectTo;
    }

    /**
     * @param brachListSelectTo the brachListSelectTo to set
     */
    public void setBrachListSelectTo(Branch brachListSelectTo) {
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

}
