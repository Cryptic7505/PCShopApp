# PCShop App
A java application that is aimed to be user friendly and has user in mind, where the user can select parts that they want with an interface and build/order confirmation
The application has the following functionality:
* Part picker interface (no part compatibility checks)
* Core Components and Peripherals selection
* Name,Contact, and address information storing to database
* Order/Build list stored in the database with date
* Shop Owner/Employee UI for Data Display and Stock management

## TODO
* [ ] Front end
    * [X] ConsumerUI
        * [X] Part Selection
        * [X] Build/Order Confirmation
    * [ ] EmployeeUI
        * [ ] Login
        * [ ] Information to Access Option
        * [ ] Orders List
        * [ ] Shop Inventory
    
* [ ] Back end
    * [ ] ConsumerUI information to database
        * [ ] Part Selection
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
    * [ ] EmployeeUI Functionality
        * [ ] Login Confirmation and validation
        * [ ] Information to display option
        * [ ] Order list
            * [ ] diplay Order ID,CustomerID,Date of Order,Revenue; Item Purchased (By Part Names), (Que Order)
            * [ ] Queue Order Function
        * [ ] Shop Inventory
            * [ ] Display partlist table with functionality of adding stock in selected item
