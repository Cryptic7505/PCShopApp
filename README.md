# PCShop App
A java application that is aimed to be user friendly and has user in mind, where the user can select parts that they want with an interface and build/order confirmation
The application has the following functionality:
* Part picker interface (no part compatibility checks)
* Core Components and Peripherals selection
* Name,Contact, and address information storing to database
* Order/Build list stored in the database with date
* Shop Owner/Employee UI for Data Display and Stock management

## TODO
* [X] Front end
    * [X] ConsumerUI
        * [X] Part Selection
        * [X] Build/Order Confirmation
    * [X] EmployeeUI
        * [X] Login
        * [X] Information to Access Option
        * [X] Orders List
        * [X] Shop Inventory
    
* [X] Back end
    * [X] ConsumerUI information to database
        * [X] Part Selection
            * [X] Customer Information Input
            * [X] Dynamic Price on parts
            * [X] Dynamic Total Price
            * [ ] Dynamic Part Image Display(optional)
            * [X] Finaliziation of Parts functionality
        * [X] Build/Order Confirmation
            * [X] Confirm Order
            * [X] Order information to Table Display
            * [X] Customer Information Transfer
            * [X] Store information and update to customer,partlist,orderlist table in the database
    * [X] EmployeeUI Functionality
        * [X] Login Confirmation and validation
        * [X] Information to display option
        * [X] Order list
            * [X] diplay Order ID,CustomerID,Date of Order,Revenue; Item Purchased (By Part Names), (Que Order)
            * [X] Queue Order Function
        * [X] Shop Inventory
            * [X] Display partlist table with functionality of adding stock in selected item
