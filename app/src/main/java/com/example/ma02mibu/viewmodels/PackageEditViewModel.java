package com.example.ma02mibu.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ma02mibu.model.PackageCreateDto;
import com.example.ma02mibu.model.PackageEditDto;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;

import java.util.ArrayList;

public class PackageEditViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Product>> sharedProducts = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Service>> sharedServices = new MutableLiveData<>();
    private final MutableLiveData<PackageEditDto> packageEditDto = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Product>> getProducts(){
        return sharedProducts;
    }
    public void setProducts(ArrayList<Product> products){
        sharedProducts.postValue(products);
    }

    public MutableLiveData<ArrayList<Service>> getServices(){
        return sharedServices;
    }
    public void setServices(ArrayList<Service> services){
        sharedServices.postValue(services);
    }

    public MutableLiveData<PackageEditDto> getPackage(){
        return packageEditDto;
    }
    public void setPackage(PackageEditDto packageDto){
        this.packageEditDto.setValue(packageDto);
    }
}
