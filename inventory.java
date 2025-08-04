import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class inventory {
    private static final String DB_URL="jdbc:mysql://localhost:3306/Inventory_Management_System";
     private static final String USER="root";
     private static final String PASS="livanthan_2907";
     Scanner sc = new Scanner(System.in);
     Connection conn=null;
    inventory(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(DB_URL, USER, PASS);
            
        } catch (SQLException se) {
            se.printStackTrace();
            System.err.println("Database connection failed. Check your DB_URL, USER, PASS, and ensure MySQL is running.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            System.err.println("JDBC Driver not found. Make sure mysql-connector-j-x.x.x.jar is in your classpath.");
        }
    }

    public void start(){
        int a=0;
       do{
        displayMenu();
         a=sc.nextInt();
        switch(a){
            case 1:
                add();
                 break;
            case 2:
                     view();
                     break;
            case 3:
              update();
              break;
            case 4:
            delete();
            break;

            case 5:
            close();
            break;
        }
    }while(a!=5);

    }
     private void displayMenu() {
        System.out.println("1. Add Product");
        System.out.println("2. View All Products");
        System.out.println("3. Update Product Quantity/Price");
        System.out.println("4. Delete Product");
        System.out.println("5. Exit");
        System.out.println("Enter your choice:");
     }

    public void add(){
        System.out.println("Enter product name");
       sc.nextLine(); 
       String name=sc.nextLine();
         System.out.println("Enter product quantity");
       int quanity=sc.nextInt();
         System.out.println("Enter product price");
       double price=sc.nextInt();
       String insertSQL="INSERT INTO product(name, quanity,price) VALUES (?,?,?)";
       try(Connection conn =DriverManager.getConnection(DB_URL,USER,PASS);
       PreparedStatement insertStmt=conn.prepareStatement(insertSQL)){
            insertStmt.setString(1,name);
            insertStmt.setInt(2,quanity);
            insertStmt.setDouble(3,price);
    
            int rowsAffected=insertStmt.executeUpdate();
       }
       catch(SQLException e){
        System.out.println("error in add");

       }
       System.out.println("add successfully");
       
    }
    public void view(){
        String viewSQL="select id,name,quanity,price from product";
       try (PreparedStatement selectStmt=conn.prepareStatement(viewSQL);
                ResultSet rs=selectStmt.executeQuery())
                {
                    while(rs.next()){
                        int id=rs.getInt("id");
                        String name=rs.getString("name");
                        int quanity=rs.getInt("quanity");
                        double price=rs.getDouble("price");

                        
                        System.out.printf("ID:%d,name:%s,quanity:%d,price:$%.2f%n",id,name,quanity,price);
                    }
                }
                catch(SQLException e){
                    System.out.println("error in view");
                }
            }
public void update(){
    System.out.println("Enter product ID to update:");
    int id=sc.nextInt();
    System.out.println("Enter new quantity and price:");
    int quanity=sc.nextInt();
    System.out.println("Enter new price:");
    sc.nextLine();
    double price=sc.nextDouble();

    String updateSQL="update product SET quanity= ?,price=? where id=?";
    try(PreparedStatement updatestmt=conn.prepareStatement(updateSQL)){
        // updatestmt.setString(1,name);
        updatestmt.setInt(1,quanity);
        updatestmt.setDouble(2,price);
        updatestmt.setInt(3,id);        
        int rowsAffected=updatestmt.executeUpdate();
        if(rowsAffected > 0) {
            System.out.println("Product updated successfully.");
        }  
        else{
            System.out.println("No product found with the given ID.");
        }
    } 
        catch(SQLException e){
            System.out.println("error in update");
     }
    }
    public void delete(){
        System.out.println("Enter product ID to delete:");
        sc.nextLine();
        int id=sc.nextInt();
        sc.nextLine();
        String deleteSQL= "delete from product where id=?";
       try(PreparedStatement deletestmt=conn.prepareStatement(deleteSQL)){
        deletestmt.setInt(1,id);
        int rowsAffected=deletestmt.executeUpdate();
       }
       catch(SQLException e){
        System.out.println("error in delete");   
       }
       System.out.println("product ID"+id+"deleted successfully.");
    }
    public void close(){

    try{
    if(conn!=null){
    conn.close();
    }
     if(sc!=null){
    sc.close();
     }
    }
     catch(SQLException e){
        System.out.println("error in close");
     }

    System.out.println("Connection closed successfully.");

}
public static void main(String args[]){

    inventory ism=new inventory();
    ism.start();
}
    }

