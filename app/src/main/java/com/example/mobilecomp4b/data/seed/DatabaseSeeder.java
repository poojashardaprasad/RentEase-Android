package com.example.mobilecomp4b.data.seed;

import com.example.mobilecomp4b.data.local.dao.PropertyDao;
import com.example.mobilecomp4b.data.local.dao.UserProfileDao;
import com.example.mobilecomp4b.data.local.entity.PropertyEntity;
import com.example.mobilecomp4b.data.local.entity.UserProfileEntity;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSeeder {

    public static void seedInitialData(PropertyDao propertyDao, UserProfileDao profileDao) {
        if (propertyDao.getPropertyCount() == 0) {
            propertyDao.insertProperties(getSampleProperties());
        }

        if (profileDao.getUserProfileDirect() == null) {
            profileDao.insertOrUpdateProfile(
                new UserProfileEntity(
                    1,
                    "Alex Morgan",
                    "alex.morgan@example.com",
                    "9876543210",
                    "Both"
                )
            );
        }
    }

    private static List<PropertyEntity> getSampleProperties() {
        List<PropertyEntity> list = new ArrayList<>();

        list.add(new PropertyEntity(
            1,
            "Spacious 2 BHK Apartment",
            "A bright, comfortable home with generous living space, natural light, modular kitchen, and 24/7 security. Perfect for families looking for peace and convenience.",
            "Andheri West", "Mumbai",
            25000, 50000,
            "Flat", 2, "Families", "Semi-furnished",
            "WiFi, Parking, Elevator, Security, Power Backup",
            "img_property_1", true, 19.1363, 72.8277
        ));

        list.add(new PropertyEntity(
            2,
            "Furnished Studio for Students",
            "A practical, move-in-ready studio space close to major colleges and metro stations. Includes a cozy bed, study desk, wardrobe, and high-speed internet.",
            "Powai", "Mumbai",
            8500, 15000,
            "Studio", 1, "Students", "Furnished",
            "WiFi, Study Desk, AC, Laundry, CCTV",
            "img_property_2", true, 19.1176, 72.9060
        ));

        list.add(new PropertyEntity(
            3,
            "Modern 1 BHK Home",
            "A thoughtfully designed home with a functional kitchen, well-ventilated bedroom, and easy access to local markets and transit.",
            "Bandra West", "Mumbai",
            15000, 30000,
            "Flat", 1, "Both", "Semi-furnished",
            "Modular Kitchen, Balcony, Elevator, Intercom",
            "img_property_3", true, 19.0596, 72.8295
        ));

        list.add(new PropertyEntity(
            4,
            "Cozy Shared Room near University",
            "Twin sharing PG accommodation for university students. Includes daily room cleaning, meals options, high-speed WiFi, and 24-hour warden.",
            "Koramangala", "Bengaluru",
            6500, 10000,
            "Shared Room", 1, "Students", "Furnished",
            "WiFi, Food Provided, Housekeeping, Water Purifier",
            "img_property_4", true, 12.9352, 77.6245
        ));

        list.add(new PropertyEntity(
            5,
            "Luxury 3 BHK Independent House",
            "Spacious standalone bungalow with private garden, garage, servant room, and modern interior architecture. Ideal for large families.",
            "Indiranagar", "Bengaluru",
            45000, 100000,
            "Independent House", 3, "Families", "Furnished",
            "Garden, Garage, Power Backup, Solar Water, Pet Friendly",
            "img_property_5", true, 12.9784, 77.6408
        ));

        list.add(new PropertyEntity(
            6,
            "Private Room in Premium PG",
            "Single private room in a modern co-living space. Fully air-conditioned with attached bathroom, smart TV, and common lounge access.",
            "HSR Layout", "Bengaluru",
            12000, 20000,
            "Private Room", 1, "Students", "Furnished",
            "AC, Smart TV, Gym Access, WiFi, Biometric Entry",
            "img_property_6", true, 12.9121, 77.6446
        ));

        list.add(new PropertyEntity(
            7,
            "Affordable 1 BHK Flat",
            "Clean, unfurnished 1 BHK apartment suitable for small families or working singles. Close to bus stop and shopping complex.",
            "Whitefield", "Bengaluru",
            11000, 25000,
            "Flat", 1, "Both", "Unfurnished",
            "24/7 Water, Parking, Security Guard",
            "img_property_7", true, 12.9698, 77.7499
        ));

        list.add(new PropertyEntity(
            8,
            "Executive 2 BHK Gated Community",
            "Premium 2 BHK apartment inside a gated society featuring swimming pool, clubhouse, badminton court, and lush green gardens.",
            "Cyber City", "Gurugram",
            32000, 64000,
            "Flat", 2, "Families", "Furnished",
            "Swimming Pool, Clubhouse, Gym, Security, Covered Parking",
            "img_property_8", true, 28.4950, 77.0895
        ));

        list.add(new PropertyEntity(
            9,
            "Student Friendly Single PG Room",
            "Well-lit room with bed, mattress, wardrobe, and study table. Located 5 minutes walking distance from North Campus colleges.",
            "North Campus", "Delhi",
            7500, 10000,
            "PG", 1, "Students", "Furnished",
            "WiFi, Meals, RO Water, Power Backup",
            "img_property_1", true, 28.6903, 77.2104
        ));

        list.add(new PropertyEntity(
            10,
            "Sunny Studio Apartment near IT Park",
            "Compact studio with modular kitchen, private balcony, and air conditioning. Ideal for IT professionals and graduate students.",
            "Hinjewadi", "Pune",
            13500, 25000,
            "Studio", 1, "Both", "Furnished",
            "AC, Modular Kitchen, Balcony, Elevator",
            "img_property_2", true, 18.5912, 73.7389
        ));

        list.add(new PropertyEntity(
            11,
            "Spacious 3 BHK Family Residence",
            "Large family apartment with 3 bedrooms, 3 bathrooms, spacious living room, and high-speed lift.",
            "Kothrud", "Pune",
            28000, 60000,
            "Flat", 3, "Families", "Semi-furnished",
            "3 Bathrooms, Covered Parking, Security, Water Tank",
            "img_property_3", true, 18.5074, 73.8077
        ));

        list.add(new PropertyEntity(
            12,
            "Budget Shared PG Bed",
            "Triple sharing room for students looking for an affordable stay. Clean environment and quiet study atmosphere.",
            "Viman Nagar", "Pune",
            5000, 8000,
            "Shared Room", 1, "Students", "Furnished",
            "WiFi, Hot Water, Daily Cleaning",
            "img_property_4", true, 18.5679, 73.9143
        ));

        list.add(new PropertyEntity(
            13,
            "Serene 2 BHK Independent House",
            "Quiet independent house with front porch, back yard, and open terrace. Located in a family-friendly peaceful neighborhood.",
            "Salt Lake", "Kolkata",
            18000, 36000,
            "Independent House", 2, "Families", "Semi-furnished",
            "Private Terrace, Garden, Car Parking",
            "img_property_5", true, 22.5867, 88.4171
        ));

        list.add(new PropertyEntity(
            14,
            "Girls Deluxe PG Accommodation",
            "Safe and secure PG facility exclusively for female students and working women. CCTV monitoring and biometric access.",
            "Banjara Hills", "Hyderabad",
            9000, 15000,
            "PG", 1, "Students", "Furnished",
            "24/7 Security, Biometric Lock, Food, WiFi, Washing Machine",
            "img_property_6", true, 17.4156, 78.4347
        ));

        list.add(new PropertyEntity(
            15,
            "Modern 2 BHK Flat near Financial District",
            "Well-maintained flat in high-rise building with great skyline views and close proximity to corporate hubs.",
            "Gachibowli", "Hyderabad",
            26000, 50000,
            "Flat", 2, "Both", "Furnished",
            "Gym, Lift, Power Backup, Swimming Pool",
            "img_property_7", true, 17.4401, 78.3489
        ));

        list.add(new PropertyEntity(
            16,
            "Compact 1 BHK Studio for Rent",
            "Self-contained 1 BHK with separate kitchen space and balcony. Excellent connectivity to metro line.",
            "Malleswaram", "Bengaluru",
            14000, 28000,
            "Studio", 1, "Both", "Semi-furnished",
            "Metro Connectivity, Modular Kitchen, 24/7 Water",
            "img_property_8", true, 13.0031, 77.5643
        ));

        list.add(new PropertyEntity(
            17,
            "Co-Living Private Suite",
            "High-end co-living studio room with workspace, attached bath, minifridge, and access to common lounge & cafeteria.",
            "Jubilee Hills", "Hyderabad",
            16000, 30000,
            "Private Room", 1, "Students", "Furnished",
            "Mini Fridge, Workstation, High Speed Internet, Cafeteria",
            "img_property_1", true, 17.4319, 78.4073
        ));

        list.add(new PropertyEntity(
            18,
            "4 BHK Luxury Villa",
            "Palatial 4 bedroom villa with private pool, lawn, servant quarters, double garage, and smart home automation.",
            "ECR Road", "Chennai",
            65000, 150000,
            "Independent House", 4, "Families", "Furnished",
            "Private Pool, Lawn, Smart Home, Double Garage, Security",
            "img_property_2", true, 12.8794, 80.2452
        ));

        list.add(new PropertyEntity(
            19,
            "Student Hub Shared Apartment Bed",
            "Shared bed in a 3 BHK student apartment shared with college peers. Kitchen facilities available for self-cooking.",
            "Adyar", "Chennai",
            6000, 10000,
            "Shared Room", 1, "Students", "Furnished",
            "WiFi, Kitchen Access, Refrigerator, Washing Machine",
            "img_property_3", true, 13.0012, 80.2565
        ));

        list.add(new PropertyEntity(
            20,
            "Comfortable 2 BHK Apartment",
            "Conveniently located 2 BHK flat near hospital, schools, and shopping center. Low maintenance costs.",
            "Velachery", "Chennai",
            19000, 40000,
            "Flat", 2, "Families", "Semi-furnished",
            "Car Parking, Security, Lift, Water Filter",
            "img_property_4", true, 12.9759, 80.2212
        ));

        list.add(new PropertyEntity(
            21,
            "Cozy PG for Boys",
            "Clean single bed PG near engineering colleges. Includes breakfast and dinner.",
            "Sector 62", "Noida",
            7000, 10000,
            "PG", 1, "Students", "Furnished",
            "Meals, WiFi, RO Water, Cleaning",
            "img_property_5", true, 28.6280, 77.3649
        ));

        list.add(new PropertyEntity(
            22,
            "Modern 1 BHK Apartment",
            "Recently renovated 1 BHK flat with wooden flooring and sleek modern fittings.",
            "Ghatkopar East", "Mumbai",
            17500, 35000,
            "Flat", 1, "Both", "Furnished",
            "Wooden Flooring, Modular Kitchen, AC, Security",
            "img_property_6", true, 19.0860, 72.9080
        ));

        list.add(new PropertyEntity(
            23,
            "Peaceful 2 BHK Independent Floor",
            "First floor of a 2-storey house with separate entrance, spacious balcony, and open terrace access.",
            "Dwarka", "Delhi",
            21000, 42000,
            "Independent House", 2, "Families", "Unfurnished",
            "Separate Entrance, Balcony, Terrace Access, Parking",
            "img_property_7", true, 28.5921, 77.0460
        ));

        list.add(new PropertyEntity(
            24,
            "Premium Student Studio Suite",
            "All-inclusive student suite with desk, ergonomic chair, kitchenette, micro-oven, and high-speed Wi-Fi.",
            "Manipal University Area", "Manipal",
            9500, 15000,
            "Studio", 1, "Students", "Furnished",
            "Kitchenette, Microwave, Study Table, WiFi, Laundry Service",
            "img_property_8", true, 13.3525, 74.7928
        ));

        list.add(new PropertyEntity(
            25,
            "3 BHK High-Rise Flat",
            "Luxurious 3 BHK flat on the 14th floor with panoramic city views and top-of-the-line amenities.",
            "Thane West", "Mumbai",
            38000, 80000,
            "Flat", 3, "Families", "Furnished",
            "Panoramic Views, Gym, Swimming Pool, High Speed Elevators, Tennis Court",
            "img_property_1", true, 19.2183, 72.9781
        ));

        return list;
    }
}
