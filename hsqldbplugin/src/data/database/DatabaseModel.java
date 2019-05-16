package data.database;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Daniel Plaza
 */
public abstract class DatabaseModel {
    
    //database common keys
    protected static final String KEY_ID = "id";
    protected static final String KEY_NAME = "name";

    //table invoice
    protected static final String KEY_DATE = "date";
    protected static final String KEY_AMOUNT_TOTAL = "amount_total";
    protected static final String KEY_AMOUNT_TAX = "amount_tax";
    protected static final String KEY_AMOUNT_UNTAXED = "amount_untaxed";

    //table invoice_line
    protected static final String KEY_INVOICE_ID = "invoice_id";
    protected static final String KEY_PRODUCT_ID = "product_id";
    protected static final String KEY_PRODUCT_QTY = "product_qty";

    //table product_product
    protected static final String KEY_DESCRIPTION = "description";
    protected static final String KEY_ACTIVE = "active";
    protected static final String KEY_QTY_AVAILABLE = "qty_available";
    protected static final String KEY_PRICE_UNIT = "price_unit";
    protected static final String KEY_CATEG_ID = "categ_id";

    //table account_tax
    protected static final String KEY_TAX_ID = "tax_id";
    protected static final String KEY_AMOUNT = "amount";

    //table product_category
    
    //database table names
    protected static final String TABLE_INVOICE = "invoice";
    protected static final String TABLE_INVOICE_LINE = "invoice_line";
    protected static final String TABLE_PRODUCT_PRODUCT = "product_product";
    protected static final String TABLE_ACCOUNT_TAX = "account_tax";
    protected static final String TABLE_PRODUCT_CATEGORY = "product_category";

    //database settings
    protected final String DATABASE_PATH;
    protected final String DATABASE_NAME;
    protected final String DATABASE_USER;
    protected final String DATABASE_PASSWORD;

    public DatabaseModel(String path, String name, String user, String password) {
        DATABASE_PATH = path;
        DATABASE_NAME = name;
        DATABASE_USER = user;
        DATABASE_PASSWORD = password;
    }

    protected abstract Connection getConnection();

    /*
    * Inserts and updates
    */
    
    public abstract boolean insertOrUpdateIntoAccountTax(AccountTax accountTax);

    public abstract boolean insertOrUpdateIntoProductCategory(ProductCategory productCategory);

    public abstract boolean insertOrUpdateIntoProductProduct(ProductProduct productProduct);

    public abstract boolean insertOrUpdateIntoInvoiceLine(InvoiceLine invoiceLine);

    public abstract boolean insertOrUpdateIntoInvoice(Invoice invoice);
    
    /*
    * Deletes
    */
    
    public abstract boolean deleteFromAccountTax(AccountTax accountTax);
    public abstract boolean deleteFromProductCategory(ProductCategory productCategory);
    public abstract boolean deleteFromProductProduct(ProductProduct productProduct);
    public abstract boolean deleteFromInvoiceLine(InvoiceLine invoiceLine); 
    public abstract boolean deleteFromInvoice(Invoice invoice);
    
    /*
    * Queries
    */
    
    public abstract HashMap<Integer,AccountTax> getAccountTax();
    public abstract HashMap<Integer,ProductProduct> getProductProduct();
    public abstract HashMap<Integer,ProductCategory> getProductCategory();
    public abstract HashMap<Integer,InvoiceLine> getInvoiceLine();
    public abstract HashMap<Integer,Invoice> getInvoice();
    
    /*
    * MO
    */

    public static class AccountTax {

        private int id;
        private String name;
        private double amount;

        public AccountTax() {
        }

        public AccountTax(int id, String name, double amount) {
            this.id = id;
            this.name = name;
            this.amount = amount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

    }

    public static class ProductCategory {

        private int id;
        private String name;

        public ProductCategory() {
        }

        public ProductCategory(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class ProductProduct {

        private int id;
        private String name;
        private String description;
        private boolean active;
        private int taxId;
        private int categId;
        private double qtyAvailable;
        private double priceUnit;

        public ProductProduct() {
        }

        public ProductProduct(int id, String name, String description, boolean active, int taxId, int categId,double qtyAvailable, double priceUnit) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.active = active;
            this.taxId = taxId;
            this.categId = categId;
            this.qtyAvailable = qtyAvailable;
            this.priceUnit = priceUnit;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public int getTaxId() {
            return taxId;
        }

        public void setTaxId(int taxId) {
            this.taxId = taxId;
        }

        public int getCategId() {
            return categId;
        }

        public void setCategId(int categId) {
            this.categId = categId;
        }
        
        public double getQtyAvailable() {
            return qtyAvailable;
        }

        public void setQtyAvailable(double qtyAvailable) {
            this.qtyAvailable = qtyAvailable;
        }

        public double getPriceUnit() {
            return priceUnit;
        }

        public void setPriceUnit(double priceUnit) {
            this.priceUnit = priceUnit;
        }

    }

    public static class InvoiceLine {

        private int id;
        private int invoiceId;
        private int productId;
        private double productQty;
        private double priceUnit;
        private int taxId;

        public InvoiceLine() {
        }

        public InvoiceLine(int id, int invoiceId, int productId, double productQty, double priceUnit,int taxId) {
            this.id = id;
            this.invoiceId = invoiceId;
            this.productId = productId;
            this.productQty = productQty;
            this.priceUnit = priceUnit;
            this.taxId=taxId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(int InvoiceId) {
            this.invoiceId = InvoiceId;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public double getProductQty() {
            return productQty;
        }

        public void setProductQty(double productQty) {
            this.productQty = productQty;
        }

        public double getPriceUnit() {
            return priceUnit;
        }

        public void setPriceUnit(double priceUnit) {
            this.priceUnit = priceUnit;
        }

        public int getTaxId() {
            return taxId;
        }

        public void setTaxId(int taxId) {
            this.taxId = taxId;
        }
    }

    public static class Invoice {

        private int id;
        private String name;
        private Date date;
        private double amountTotal;
        private double amountTax;
        private double amountUntaxed;

        public Invoice() {
        }

        public Invoice(int id, String name, Date date, double amountTotal, double amountTax, double amountUntaxed) {
            this.id = id;
            this.name = name;
            this.date = date;
            this.amountTotal = amountTotal;
            this.amountTax = amountTax;
            this.amountUntaxed = amountUntaxed;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public double getAmountTotal() {
            return amountTotal;
        }

        public void setAmountTotal(double amountTotal) {
            this.amountTotal = amountTotal;
        }

        public double getAmountTax() {
            return amountTax;
        }

        public void setAmountTax(double amountTax) {
            this.amountTax = amountTax;
        }

        public double getAmountUntaxed() {
            return amountUntaxed;
        }

        public void setAmountUntaxed(double amountUntaxed) {
            this.amountUntaxed = amountUntaxed;
        }
        
        
    }
}
