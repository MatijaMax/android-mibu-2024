package com.example.ma02mibu.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ma02mibu.model.Product;

import java.util.ArrayList;

public class CategorySharedViewModel extends ViewModel {
    private final MutableLiveData<String> category = new MutableLiveData<>();

    public MutableLiveData<String> getCategory(){
        return category;
    }
    public void setCategory(String cat){
        category.postValue(cat);
    }

}
