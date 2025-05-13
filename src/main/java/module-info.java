/**
 * Module declaration for the Whiskey Information System (WIS) application.
 *
 * This module serves as the entry point and core configuration for a JavaFX-based
 * desktop application that enables users to query and view whiskey data based on
 * various attributes such as distillery, age, region, and price.
 *
 * @author Ayush Bhandari S12157470
 */
module cqu.wis {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.base; 
  requires java.sql;
  opens cqu.wis to javafx.fxml;
  exports cqu.wis;
  opens cqu.wis.view to javafx.fxml;
  exports cqu.wis.view;
}
