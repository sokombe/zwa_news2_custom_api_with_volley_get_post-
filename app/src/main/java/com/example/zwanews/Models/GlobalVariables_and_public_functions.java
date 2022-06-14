package com.example.zwanews.Models;

import java.util.HashMap;
import java.util.Map;

public class GlobalVariables_and_public_functions {

// public variables ################################################################################
  public static   Map<String, Object> UsersProfiles=new HashMap<>();



  // public functions###############################################################################
 public static String getfrench(String eng){
    String retur="";

    switch (eng){
      case "general":
        retur="Géneral";
        break;
      case "business":
        retur="Busness";
        break;
      case "entertainment":
        retur="Loisir";
        break;

      case "health":
        retur="Santé";
        break;
      case "science":
        retur="Science";
        break;
      case "sports":
        retur="Sports";
        break;
      case "technology":
        retur="Technologies";
    }
    return  retur;
  }



}
