
package data.database.ext;


import data.database.DatabaseModel.AccountTax;

/**
 *
 * @author Daniel Plaza
 */
public class AccountTaxExt extends AccountTax{
    
    public AccountTaxExt(AccountTax at){
        this.setId(at.getId());
        this.setName(at.getName());
        this.setAmount(at.getAmount());
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
}
