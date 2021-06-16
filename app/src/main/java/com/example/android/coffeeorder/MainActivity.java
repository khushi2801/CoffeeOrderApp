package com.example.android.coffeeorder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // To obtain name
        EditText nameEntered = (EditText) findViewById(R.id.name);
        String name = nameEntered.getText().toString();

        // Valid Email pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // To obtain address
        EditText addressEntered = (EditText) findViewById(R.id.address);
        String address = addressEntered.getText().toString();

        // To obtain email
        EditText emailEntered = (EditText) findViewById(R.id.email);
        String emailTo = emailEntered.getText().toString();

        // Figure out if the user wants whipped cream topping
        CheckBox whippedCream = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean whippedCreamChecked = whippedCream.isChecked();

        // Figure out if the user wants chocolate topping
        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean chocolateChecked = chocolate.isChecked();

        String message = createOrderSummary(name, address, whippedCreamChecked, chocolateChecked);

        /* if (!emailTo.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailTo).matches()) {
            Toast.makeText(this, "Email Verified !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
        } */

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ emailTo });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order summary for " + name);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if(quantity == 100) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity += 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if(quantity == 1) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity -= 1;
        displayQuantity(quantity);
    }

    /**
     * Calculates the price of the order.
     *
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate is whether or not the user wants chocolate topping
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        // Price of 1 cup of coffee
        int basePrice = 20;

        // Add Rs.1 if user wants Whipped Cream
        if(hasWhippedCream)
            basePrice += 5;

        // Add Rs.2 if user wants Chocolate
        if(hasChocolate)
            basePrice += 10;

        // Calculate the totalPrice
        return (quantity * basePrice);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * Create summary of the order.
     *
     * @param name is name entered
     * @param whippedCream is whether or not the user wants whipped cream topping
     * @param chocolate is whether or not the user wants chocolate topping
     * @return text summary
     */
    private String createOrderSummary(String name, String address, boolean whippedCream, boolean chocolate) {
        String message = "Name :  " + name;
        message += "\nAddress :  " + address;
        if(whippedCream) {
            message += "\nAdd Whipped Cream?  Yes";
        } else {
            message += "\nAdd Whipped Cream?  No";
        }
        if(chocolate) {
            message += "\nAdd Chocolate?  Yes";
        } else {
            message += "\nAdd Chocolate?  No";
        }
        message += "\nQuantity :  " + quantity;
        message += "\nTotal :  Rs. " + calculatePrice(whippedCream, chocolate);
        message += "\nThank You!";
        return message;
    }
}