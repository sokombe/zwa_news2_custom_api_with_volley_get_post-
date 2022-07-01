package com.example.zwanews.Models;

import java.util.HashMap;
import java.util.Map;

public class GlobalVariables_and_public_functions {

// public variables ################################################################################
  public static   Map<String, Object> UsersProfiles=new HashMap<>();
  //public  static Users userProfile=new Users();


  // public functions###############################################################################
 public static String getcatname(String id){
    String retur="";

    switch (id){
      case "1":
        retur="Géneral";
        break;
      case "2":
        retur="Busness";
        break;
      case "3":
        retur="Loisir";
        break;

      case "4":
        retur="Santé";
        break;
      case "5":
        retur="Science";
        break;
      case "6":
        retur="Sports";
        break;
      case "7":
        retur="Technologies";
    }
    return  retur;
  }



}
