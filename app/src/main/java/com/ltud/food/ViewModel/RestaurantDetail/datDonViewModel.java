package com.ltud.food.ViewModel.RestaurantDetail;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Food;
import com.ltud.food.Model.Order_Food;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.Repository.RestaurantDetail.datDonRepository;

import java.util.List;

public class datDonViewModel extends ViewModel {

    private final datDonRepository repository = datDonRepository.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String addOneOrder(Restaurant restaurant, Food food)
    {
        return repository.addOneOrder(restaurant, food);
    }

    public void addFood(String orderID, Food food)
    {
        repository.addFood(orderID, food);
    }

}
