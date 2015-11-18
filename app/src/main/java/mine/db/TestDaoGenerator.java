package mine.db;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by jacky on 15/10/5.
 */
public class TestDaoGenerator {
    public static void main(String[] agrs){
        Schema schema = new Schema(1, "com.example.jacky.testgreendao.modle");
        //addNode(schema);
        //addCustomerOrder(schema);
       // addUser(schema);
        //addDemo(schema);
        addNote(schema);
        addCustomerOrder(schema);
        try {
            new DaoGenerator().generateAll(schema,
                    "/Users/jacky/androiddvp/prjs/github/playdemo/GeenDaoDemo/app/src/main/java/");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("hello world");
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }

    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        //customer.addLongProperty("customerId").primaryKey();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();

        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }

    private static void addDemo(Schema schema){
        Entity user = schema.addEntity("user");
        user.addIdProperty();
        user.addStringProperty("phonenum").primaryKey();
        Property property = user.addLongProperty("picId").getProperty();

        Entity pic =  schema.addEntity("pic");
        pic.addIdProperty();
        //pic.addStringProperty("picId").primaryKey();
        pic.addStringProperty("url");
        user.addToOne(pic, property);
    }

   /* private static void addNode(Schema schema){

        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addLongProperty("date");
        note.addStringProperty("extrals");
    }*/

    private static void addUser(Schema schema){
        Entity customer = schema.addEntity("customer");
        customer.setTableName("t_user");
        customer.addIdProperty();
        customer.addStringProperty("account").unique();
        customer.addStringProperty("password");
        customer.addDateProperty("birthday");
        customer.addShortProperty("gender");
        customer.addIntProperty("height");
        customer.addFloatProperty("weight");
        customer.addDateProperty("registerTime");
        customer.implementsInterface("Jsonable<User>");

    }
/*
    private static void addCustomerOrder(Schema schema){

        Entity customer= schema.addEntity("Customer");
        customer.addIdProperty();

        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS");
        order.addIdProperty();

        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);


        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }*/
}
