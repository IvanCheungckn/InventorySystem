# WarehouseInventorySystem

 Frontend is completed with React (Typescript)

 Backend is completed with Spring Boot, MySQL and completed with unit testing by Mockito, Junit and PowerMock

## Get Started

### Frontend:
1. Go to "frontend" directory
2. `yarn install`
3. `yarn start`

### Backend:
1. Create a MySQL database called **warehouse_inventory**
2. Go to "backend" directory
3. find **application.properties** in main/resources/
4.  change the properties below according to your settings.
```
spring.datasource.username=xxxx
spring.datasource.user=xxxx
spring.datasource.password=xxxx
```
2. `./mvnw spring-boot:run`

### Run Test
1. Go to "backend" directory
2. Since some test cases used with PowerMock, these test cases will be skipped if using `./mvnw clean test`. So, need to open the folder with intellij / Eclipse, right click **test/java folder** to run test.


Once started frontend and backend applications, visit `http://localhost:3000`. 

There will be some seed data for illustration. 

In the base directory, there is a csv file called **sample.csv**. This sample csv file is a demo format for updating inventory quantity of products or update the product name and product weight.


### Functions
1. Update / Transfer Inventory Quantity by UI
2. Update / Transfer Inventory Quantity by csv
3. Update Product Information by csv
4. Create Product by UI
5. Delete Product by UI
6. Search Products by multiple columns
7. Sort Products by different columns

### p.s.
In order to prevent any accidentally overwrite of existing products, the product code is created and managed by the system. To update product information by csv, the products in csv must be stored in Database already. If not, creating the product is needed in advance. 

### Diary
I was thinking to update the quantity or data row by row per Service Method. But, later, I found that if I don't put all of the update within one Service Method, it will cause a problem that some data updated and some are not when having Runtime errors. But, if update data in one Service Method, due to the `@Transactional` annotation, any Runtime errors happened will rollback.
I did not mock a static class before, but by doing this test, I leant that it is needed to be mocked with PowerMock but not Mockito and how to mock it with PowerMock to finish the unit test.
