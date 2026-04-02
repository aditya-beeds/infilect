package com.aditya.infilect.service;

import com.aditya.infilect.dto.StoreMasterDTO;
import com.aditya.infilect.storemasterdb.entity.*;
import com.aditya.infilect.storemasterdb.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreMasterService {
    @Autowired
    private StoreMasterRepository storeMasterRepository;
    @Autowired
    private StoreBrandsRepository storeBrandRepository;
    @Autowired
    private StoreTypesRepository storeTypeRepository;
    @Autowired
    private CitiesRepository cityRepository;
    @Autowired
    private StatesRepository stateRepository;
    @Autowired
    private CountriesRepository countryRepository;
    @Autowired
    private RegionsRepository regionRepository;

    @Transactional
    public StoreMaster createStore(StoreMasterDTO dto) {

        StoreMaster store = new StoreMaster();

        store.setStoreId(dto.getStoreId().trim());
        store.setStoreExternalId(dto.getStoreExternalId() != null ? dto.getStoreExternalId().trim() : "");
        store.setName(dto.getName().trim());
        store.setTitle(dto.getTitle().trim());
        store.setLatitude(dto.getLatitude());
        store.setLongitude(dto.getLongitude());


        if (dto.getStoreBrand() != null && !dto.getStoreBrand().trim().isEmpty()) {
            String brandName = dto.getStoreBrand().trim();
            StoreBrands brand = storeBrandRepository.findByNameIgnoreCase(brandName)
                    .orElseGet(() -> {
                        StoreBrands newBrand = new StoreBrands();
                        newBrand.setName(brandName);
                        return storeBrandRepository.save(newBrand);
                    });
            store.setStoreBrands(brand);
        }

        // Handle Store Type
        if (dto.getStoreType() != null && !dto.getStoreType().trim().isEmpty()) {
            String typeName = dto.getStoreType().trim();
            StoreTypes type = storeTypeRepository.findByNameIgnoreCase(typeName)
                    .orElseGet(() -> {
                        StoreTypes newType = new StoreTypes();
                        newType.setName(typeName);
                        return storeTypeRepository.save(newType);
                    });
            store.setStoreType(type);
        }

        // Handle City
        if (dto.getCity() != null && !dto.getCity().trim().isEmpty()) {
            String cityName = dto.getCity().trim();
            Cities city = cityRepository.findByNameIgnoreCase(cityName)
                    .orElseGet(() -> {
                        Cities newCity = new Cities();
                        newCity.setName(cityName);
                        return cityRepository.save(newCity);
                    });
            store.setCity(city);
        }


        if (dto.getState() != null && !dto.getState().trim().isEmpty()) {
            String stateName = dto.getState().trim();
            States state = stateRepository.findByNameIgnoreCase(stateName)
                    .orElseGet(() -> {
                        States newState = new States();
                        newState.setName(stateName);
                        return stateRepository.save(newState);
                    });
            store.setState(state);
        }


        if (dto.getCountry() != null && !dto.getCountry().trim().isEmpty()) {
            String countryName = dto.getCountry().trim();
            Countries country = countryRepository.findByNameIgnoreCase(countryName)
                    .orElseGet(() -> {
                        Countries newCountry = new Countries();
                        newCountry.setName(countryName);
                        return countryRepository.save(newCountry);
                    });
            store.setCountry(country);
        }


        if (dto.getRegion() != null && !dto.getRegion().trim().isEmpty()) {
            String regionName = dto.getRegion().trim();
            Regions region = regionRepository.findByNameIgnoreCase(regionName)
                    .orElseGet(() -> {
                        Regions newRegion = new Regions();
                        newRegion.setName(regionName);
                        return regionRepository.save(newRegion);
                    });
            store.setRegion(region);
        }

        return storeMasterRepository.save(store);
    }

}
