package com.example.numad24fa_wenxinqi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CalculatorActivity extends AppCompatActivity {
    private TextView calcText;
    private StringBuilder input = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        calcText = findViewById(R.id.textViewCalc);

        // Set up buttons
        setupButton(R.id.button0, "0");
        setupButton(R.id.button1, "1");
        setupButton(R.id.button2, "2");
        setupButton(R.id.button3, "3");
        setupButton(R.id.button4, "4");
        setupButton(R.id.button5, "5");
        setupButton(R.id.button6, "6");
        setupButton(R.id.button7, "7");
        setupButton(R.id.button8, "8");
        setupButton(R.id.button9, "9");
        setupButton(R.id.buttonPlus, "+");
        setupButton(R.id.buttonMinus, "-");


        findViewById(R.id.buttonX).setOnClickListener(v -> {
            if (input.length() > 0) {
                input.deleteCharAt(input.length() - 1);
                calcText.setText(input.toString());
            }
        });

        findViewById(R.id.buttonEquals).setOnClickListener(v -> {
            // Evaluate the expression
            try {
                int result = evaluateExpression(input.toString());
                calcText.setText(String.valueOf(result));
                input.setLength(0);
                input.append(result);
            } catch (Exception e) {
                calcText.setText("Error");
            }
        });
    }

    private void setupButton(int id, String value) {
        findViewById(id).setOnClickListener(v -> {
            input.append(value);
            calcText.setText(input.toString());
        });
    }

    private int evaluateSimpleExpression(String expression) {
        int result = 0;
        StringBuilder number = new StringBuilder(); // To build the current number
        char lastOperator = '+'; // Default to addition for the first number

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // If the current character is a digit, add it to the current number
            if (Character.isDigit(c) ) {
                number.append(c); // Collect the current number
            }

            // If the current character is an operator or we've reached the end of the string
            if (!Character.isDigit(c) || i == expression.length() - 1) {

                // Parse the current number
                if (number.length() > 0) {
                    int currentNumber = Integer.parseInt(number.toString());

                    // Apply the last operator to the result
                    if (lastOperator == '+') {
                        result += currentNumber;
                    } else if (lastOperator == '-') {
                        result -= currentNumber;
                    }

                    // Clear the number builder for the next number
                    number.setLength(0);
                }

                // Update the last operator if it's an operator (+ or -)
                if (c == '+' || c == '-') {
                    lastOperator = c;  // This ensures lastOperator is properly updated
                }
            }
        }

        return result;
    }






    private int evaluateExpression(String expression) {
        try {
            // Remove spaces from the expression (if any)
            expression = expression.replaceAll("\\s+", "");

            // Check if the expression is valid
            if (expression.isEmpty()) {
                return 0;
            }

            // Split the expression into terms (numbers and operators)
            return evaluateSimpleExpression(expression);
        } catch (Exception e) {
            // Handle any parsing or evaluation errors
            throw new IllegalArgumentException("Invalid expression");
        }
    }




}
