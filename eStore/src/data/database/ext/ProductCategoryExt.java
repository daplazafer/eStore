/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data.database.ext;

import data.database.DatabaseModel.ProductCategory;

/**
 *
 * @author Daniel Plaza
 */
public class ProductCategoryExt extends ProductCategory{
    
    public ProductCategoryExt(ProductCategory pc){
        this.setId(pc.getId());
        this.setName(pc.getName());
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
}
