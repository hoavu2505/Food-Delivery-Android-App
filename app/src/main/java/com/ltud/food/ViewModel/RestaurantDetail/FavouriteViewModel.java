package com.ltud.food.ViewModel.RestaurantDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ltud.food.Model.Favourite;
import com.ltud.food.Model.Restaurant;
import com.ltud.food.Repository.RestaurantDetail.FavouriteRepository;

import java.util.List;

public class FavouriteViewModel extends ViewModel {
    private final FavouriteRepository favouriteRepository = FavouriteRepository.getInstance();
    private MutableLiveData<Favourite> favouriteLiveData;

    public LiveData<Favourite> getCurrentFavourite(String resID){
        favouriteLiveData = favouriteRepository.getCurrentFavourite(resID);
        return favouriteLiveData;
    }

    public void addOneFavourite(Restaurant restaurant){
        Favourite favourite = favouriteRepository.addOneFavourite(restaurant);
        favouriteLiveData.setValue(favourite);
    }

    public void deleteOneFavourite(String favouriteID){
        favouriteRepository.deleteOneFavourite(favouriteID);
        Favourite favourite = favouriteLiveData.getValue();
        favouriteLiveData.setValue(favourite);
    }

}
