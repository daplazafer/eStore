package data.database;

import data.config.Config;
import data.database.DatabaseModel.AccountTax;
import data.database.DatabaseModel.Invoice;
import data.database.DatabaseModel.InvoiceLine;
import data.database.DatabaseModel.ProductCategory;
import data.database.DatabaseModel.ProductProduct;
import data.database.ext.AccountTaxExt;
import data.database.ext.InvoiceExt;
import data.database.ext.InvoiceLineExt;
import data.database.ext.ProductCategoryExt;
import data.database.ext.ProductProductExt;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import methods.Util;

/**
 *
 * @author Daniel Plaza
 */
public class DatabaseManager {

    private final DatabaseModel dbm;

    private HashMap<Integer, AccountTax> accountTaxList;
    private HashMap<Integer, ProductCategory> productCategoryList;
    private HashMap<Integer, ProductProduct> productProductList;
    private HashMap<Integer, Invoice> invoiceList;
    private HashMap<Integer, InvoiceLine> invoiceLineList;

    public DatabaseManager(Config config) {
        dbm = getDatabaseModel(config);
        reloadAccountTax();
        reloadProductCategory();
        reloadProductProduct();
        reloadInvoice();
        reloadInvoiceLine();
    }

    public boolean createInvoice(InvoiceExt ie, List<InvoiceLineExt> ilList) {
        int id = getNewId(invoiceList);
        boolean r = dbm.insertOrUpdateIntoInvoice(new Invoice(id, getNewInvoiceName(id), ie.getDate(), Util.round2d(ie.getAmountTotal()), Util.round2d(ie.getAmountTax()), Util.round2d(ie.getAmountUntaxed())));
        if (r) {
            for (InvoiceLineExt ile : ilList) {
                dbm.insertOrUpdateIntoInvoiceLine(new InvoiceLine(getNewId(invoiceLineList), id, ile.getProductId(), Util.round2d(ile.getProductQty()), Util.round2d(ile.getPriceUnit()), ile.getTaxId()));
                reloadInvoiceLine();
                ProductProduct pp = productProductList.get(ile.getProductId());
                pp.setQtyAvailable(pp.getQtyAvailable() - ile.getProductQty());
                dbm.insertOrUpdateIntoProductProduct(pp); 
                reloadProductProduct();
            }
            reloadInvoice();
            
        }
        return r;
    }

    public InvoiceExt generateNewInvoice() {
        return new InvoiceExt(new Date(), 0, 0, 0);
    }

    public boolean createProductProduct(String name, String description, boolean active, AccountTax tax, double qty, double price, ProductCategory categ) {
        boolean r = dbm.insertOrUpdateIntoProductProduct(new ProductProduct(getNewId(productProductList), name, description, active, tax.getId(), categ.getId(), qty, price));
        if (r) {
            reloadProductProduct();
        }
        return r;
    }

    public boolean updateProductProduct(int id, String name, String description, boolean active, AccountTax tax, double qty, double price, ProductCategory categ) {
        boolean r = dbm.insertOrUpdateIntoProductProduct(new ProductProduct(id, name, description, active, tax.getId(), categ.getId(), qty, price));
        if (r) {
            reloadProductProduct();
        }
        return r;
    }

    public boolean createProductCategory(String name) {
        boolean r = dbm.insertOrUpdateIntoProductCategory(new ProductCategory(getNewId(productCategoryList), name));
        if (r) {
            reloadProductCategory();
        }
        return r;
    }

    public boolean createAccountTax(String name, double amount) {
        boolean r = dbm.insertOrUpdateIntoAccountTax(new AccountTax(getNewId(accountTaxList), name, amount));
        if (r) {
            reloadAccountTax();
        }
        return r;
    }

    private int getNewId(HashMap hm) {
        int newId = 0;
        boolean v = false;
        do {
            if (!hm.containsKey(newId)) {
                v = true;
            } else {
                newId++;
            }
        } while (v == false);
        return newId;
    }

    private String getNewInvoiceName(int id) {
        return String.format("%06d", id);
    }
    
    public ArrayList<InvoiceLineExt> getInvoiceList(InvoiceExt invoice) {
        ArrayList<InvoiceLineExt> ileList = new ArrayList<>();
        Iterator it = invoiceLineList.entrySet().iterator();
        while (it.hasNext()) {
            InvoiceLine i = (InvoiceLine) ((Map.Entry) it.next()).getValue();
            if(invoice.getId()==i.getInvoiceId()){
                String productName=productProductList.get(i.getProductId()).getName();
                String taxName=accountTaxList.get(i.getTaxId()).getName();
                String categName=productCategoryList.get(productProductList.get(i.getProductId()).getCategId()).getName();
                ileList.add(new InvoiceLineExt(i,productName,taxName,categName));
            }
        }
        return ileList;
    }

    public ArrayList<InvoiceExt> getInvoiceList() {
        ArrayList<InvoiceExt> iList = new ArrayList<>();
        Iterator it = invoiceList.entrySet().iterator();
        while (it.hasNext()) {
            Invoice i = (Invoice) ((Map.Entry) it.next()).getValue();
            iList.add(new InvoiceExt(i));
        }
        return iList;
    }

    public ArrayList<ProductProductExt> getProductList() {
        ArrayList<ProductProductExt> productList = new ArrayList<>();
        Iterator it = productProductList.entrySet().iterator();
        while (it.hasNext()) {
            ProductProduct pp = (ProductProduct) ((Map.Entry) it.next()).getValue();
            productList.add(new ProductProductExt(pp, productCategoryList.get(pp.getCategId()).getName(), accountTaxList.get(pp.getTaxId()).getName(), accountTaxList.get(pp.getTaxId()).getAmount()));
        }
        return productList;
    }

    public ArrayList<ProductProductExt> getProductList(ProductCategoryExt pc) {
        ArrayList<ProductProductExt> productList = new ArrayList<>();
        Iterator it = productProductList.entrySet().iterator();
        while (it.hasNext()) {
            ProductProduct pp = (ProductProduct) ((Map.Entry) it.next()).getValue();
            if (pc.getId() == pp.getCategId() && pp.isActive()) {
                productList.add(new ProductProductExt(pp, productCategoryList.get(pp.getCategId()).getName(), accountTaxList.get(pp.getTaxId()).getName(), accountTaxList.get(pp.getTaxId()).getAmount()));
            }
        }
        return productList;
    }

    public ArrayList<ProductCategoryExt> getCategoryList() {
        ArrayList<ProductCategoryExt> categList = new ArrayList<>();
        Iterator it = productCategoryList.entrySet().iterator();
        while (it.hasNext()) {
            categList.add(new ProductCategoryExt((ProductCategory) ((Map.Entry) it.next()).getValue()));
        }
        return categList;
    }

    public ArrayList<AccountTaxExt> getTaxList() {
        ArrayList<AccountTaxExt> taxList = new ArrayList<>();
        Iterator it = accountTaxList.entrySet().iterator();
        while (it.hasNext()) {
            taxList.add(new AccountTaxExt((AccountTax) ((Map.Entry) it.next()).getValue()));
        }
        return taxList;
    }

    private void reloadAccountTax() {
        accountTaxList = dbm.getAccountTax();
    }

    private void reloadProductCategory() {
        productCategoryList = dbm.getProductCategory();
    }

    private void reloadProductProduct() {
        productProductList = dbm.getProductProduct();
    }

    private void reloadInvoice() {
        invoiceList = dbm.getInvoice();
    }

    private void reloadInvoiceLine() {
        invoiceLineList = dbm.getInvoiceLine();
    }

    private DatabaseModel getDatabaseModel(Config config) {
        try {
            URL u = new URL("file:" + Util.PlUGINS_PATH + config.getSetting(Config.SETTING.dbplugin) + ".jar");
            URL[] classLoaderUrls = {u};
            URLClassLoader loader = new URLClassLoader(classLoaderUrls, getClass().getClassLoader());
            return (DatabaseModel) Class.forName(Util.DB_PLUGIN_CLASS_NAME, true, loader).getConstructor(String.class, String.class, String.class, String.class).newInstance(config.getSetting(Config.SETTING.dbpath) + File.separator, config.getSetting(Config.SETTING.dbname), config.getSetting(Config.SETTING.dbuser), config.getSetting(Config.SETTING.dbpassword));
        } catch (Exception e) {
            return null;
        }
    }

}
