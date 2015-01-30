package com.example.andrew.calcuverter2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.ArrayList;

/* The MainActivity class contains the button listeners and methods used in the MainActivity
   or the Decimal/Hexadecimal/Binary Calculator.
 */

public class MainActivity extends ActionBarActivity {

    /* Expression holds the input expression from the user and answer holds the evaluated expression. */
    private String expression;
    private String answer;

    ArrayList<String> operands = new ArrayList<String>();
    ArrayList<String> operators = new ArrayList<String>();

    /* onCreate is used set the radio group listener and the equals button listener. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expression = "";

        /* Set listener for the radio group, used to determine if the user select decimal, hexadecimal, or binary mode. */
        final RadioGroup calculatorRadioGroup = (RadioGroup) findViewById(R.id.calculatorRadioGroup);

        Button buttonEquals = (Button) findViewById(R.id.buttonEquals);


       /* Set the listener for the equals button. */
       buttonEquals.setOnClickListener(
               new Button.OnClickListener(){

                   @Override
                   public void onClick(View v) {

                       /* Check if the user has inserted input. */
                       if (expression.equals("")) {

                           EditText calculatorInput = (EditText) findViewById(R.id.calculatorInput);
                           calculatorInput.setText("Enter an Expression");
                           return;
                       }

                       /* Clear the operands and operators lists. */
                       operands.clear();
                       operators.clear();
                       /* Temporary holder used to get each operand from the expression. */
                       String operand = "";

                       /* Loop through the input expression and get each operator and operand and
                       insert them in to the ArrayLists.
                       NEEDS UPDATE TO HANDLE NEGATIVE NUMBERS.
                        */
                       try {
                           for (int i = 0; i < expression.length(); i++) {

                           /* If an operator is found, add the operator and current operand to lists. */
                               if (Character.toString(expression.charAt(i)).equals("+") || Character.toString(expression.charAt(i)).equals("-") || Character.toString(expression.charAt(i)).equals("*")) {

                                   operators.add(Character.toString(expression.charAt(i)));
                                   operands.add(operand);
                                   operand = "";
                               } else {

                                   operand += Character.toString(expression.charAt(i));
                               }
                           }
                       } catch(Exception E) {

                           EditText calculatorInput = (EditText) findViewById(R.id.calculatorInput);
                           calculatorInput.setText("Invalid Expression Format");
                           answer = "";
                           expression = "";
                           operands.clear();
                           operators.clear();
                           return;
                       }

                       /* Add the operand from the end of the expression. */
                       operands.add(operand);

                       answer = "";

                       /* Get the select radio button (dec, hex, or bin). */
                       int selectedRadioButton = calculatorRadioGroup.getCheckedRadioButtonId();
                       boolean validFlag = true;

                       try{

                           /* Switch on the selected button. */
                           switch(selectedRadioButton) {

                               case R.id.calcDec:
                                   for (int i = 0; i < operands.size(); i++) {

                                       /* Validate to confirm decimal input is correct. */
                                       if (Validator.validateDec(operands.get(i)))
                                           continue;
                                       else
                                           validFlag = false;
                                   }

                                   /* If input is valid, call evaluate function and set answer. */
                                   if (validFlag)
                                        answer = evaluate(operators, operands);
                                   else
                                        answer = "Invalid Dec Input";
                                   break;


                               case R.id.calcHex:

                                   /* Validate hex input. */
                                   for (int i = 0; i < operands.size(); i++) {

                                       if (Validator.validateHex(operands.get(i)))
                                           continue;
                                       else
                                           validFlag = false;
                                   }

                                   if (validFlag) {

                                       /* Call convert to convert the operand list from hex to decimal. */
                                       convert("h");
                                       /* Evaluate with the converted operands. */
                                       answer = evaluate(operators, operands);
                                       /* Convert answer back to hex. */
                                       answer = Converter.decToHex(answer).toUpperCase();
                                   }

                                    else
                                        answer = "Invalid Hex Input";

                                   break;

                               case R.id.calcBin:

                                   for (int i = 0; i < operands.size(); i++) {

                                       if (Validator.validateBinary(operands.get(i)))
                                           continue;
                                       else
                                           validFlag = false;
                                   }

                                   if (validFlag) {

                                       /* Call to convert operands to binary. */
                                       convert("b");
                                       /* Evaluate with decimal operands. */
                                       answer = evaluate(operators, operands);
                                       /* Convert back to decimal. */
                                       answer = Converter.decToBin(answer).toUpperCase();
                                   }

                                   else
                                       answer = "Invalid Bin Input";

                                   break;
                           }
                       } catch(Exception e) {


                           answer = "Invalid Expression";
                       }

                       /* Output the answer to the screen. */
                       EditText calculatorInput = (EditText) findViewById(R.id.calculatorInput);
                       calculatorInput.setText(answer);

                       answer = "";
                       expression = "";
                       operands.clear();
                       operators.clear();

                   }
               }
       );

    }

    /* Evaluate takes in a list of operators and a list of operands and returns the answer
       as a string. Evaluate currently handles addition, subtraction, and multiplication.
     */
    private static String evaluate(ArrayList<String> operators, ArrayList<String> operands) {

        /* Check for valid input. There should be one more operand than operator. */
        if (operands.size() != operators.size()+1)
            return "Invalid Input";

        String current;
        int value;
        int numMultiply = 0;
        int numAdd = 0;
        int currentOperator = 0;

        /* Get the number of multiplications in the operators list. */
        for (int i = 0; i < operators.size(); i++) {

            if (operators.get(i).equals("*"))
                numMultiply++;
        }

        /* Get the number of addition and subtraction in the operators list. */
        for (int i = 0; i < operators.size(); i++) {

            if (operators.get(i).equals("-") || operators.get(i).equals("+"))
                numAdd++;
        }

        /* Evaluate multiplications first, from left to right. */
        while (numMultiply > 0){

            current = operators.get(currentOperator);
            if (current.equals("*")) {

                /* When multiplication is found in the operators, get the associative operands from the operands list.
                   Associative operands are found in the operands list at indexes current operator and current operator + 1.
                */
                value = Integer.parseInt(operands.get(currentOperator)) * Integer.parseInt(operands.get(currentOperator+1));

                /* Remove the operands and the operator since they have been evaluated and add the evaluated answer back in the operands. */
                operators.remove(currentOperator);
                operands.remove(currentOperator);
                operands.remove(currentOperator);
                operands.add(currentOperator, Integer.toString(value));

                numMultiply--;
            }
            else
                currentOperator++;
        }

        currentOperator = 0;

        /* Next evaluate the addition and subtraction, from left to right. */
        while (numAdd > 0){
            current = operators.get(currentOperator);
            if (current.equals("+")) {

                value = Integer.parseInt(operands.get(currentOperator)) + Integer.parseInt(operands.get(currentOperator+1));
                operators.remove(currentOperator);
                operands.remove(currentOperator);
                operands.remove(currentOperator);
                operands.add(currentOperator, Integer.toString(value));
                numAdd--;
            }

            else if (current.equals("-")) {

                value = Integer.parseInt(operands.get(currentOperator)) - Integer.parseInt(operands.get(currentOperator+1));
                operators.remove(currentOperator);
                operands.remove(currentOperator);
                operands.remove(currentOperator);
                operands.add(currentOperator, Integer.toString(value));
                numAdd--;
            }
            else
                currentOperator++;
        }

        /* When all the operations have been evaluated, the final answer will be in the first element of the operands list. */
        return operands.get(0);
    }

    /* Convert is used to convert input operands to decimal from either hex or bin. It takes in a string
       to indicate whether the input is in hex or bin.
     */
    private boolean convert(String type) {

        String convert = "";

        switch (type) {

            case "h":
                /* Loop through the operands and convert each to decimal. */
                for (int i = 0; i < operands.size(); i++){

                    convert = Converter.hexToDec(operands.get(i));
                    operands.set(i, convert);
                }
                break;

            case "b":
                /* Loop through the operands and convert each to decimal. */
                for (int i = 0; i < operands.size(); i++){

                    convert = Converter.binToDec(operands.get(i));
                    operands.set(i, convert);
                }
                break;
            default: return false;

        }

        return true;
    }

    public void buttonBack(View v) {

        /* Check if there is any input. */
        if (expression.equals(""))
            return;

        /* Remove the last character put in the expression. */
        expression = expression.substring(0, expression.length()-1);
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void buttonClear(View v) {

        /* Clear contents of expression and remove input from the screen. */
        expression = "";

        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);

        EditText calculatorInput = (EditText) findViewById(R.id.calculatorInput);
        calculatorInput.setText("");
    }

    /* Button listeners for the functions (plus, minus, and multiply.) */
    public void buttonPlus(View v) {

        /* Add the character to the expression and update screen. */
        expression += "+";

        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void buttonMinus(View v) {

        /* Add the character to the expression and update screen. */
        expression += "-";

        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void buttonMultiply(View v) {

        /* Add the character to the expression and update screen. */
        expression += "*";

        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    /* Button listeners for the digits (0-F). */
    public void button0(View v) {

        /* Add the digit to the expression. */
        expression += "0";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void button1(View v) {

        /* Add the digit to the expression. */
        expression += "1";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void button2(View v) {

        /* Add the digit to the expression. */
        expression += "2";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void button3(View v) {

        /* Add the digit to the expression. */
        expression += "3";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void button4(View v) {

        /* Add the digit to the expression. */
        expression += "4";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void button5(View v) {

        /* Add the digit to the expression. */
        expression += "5";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void button6(View v) {

        /* Add the digit to the expression. */
        expression += "6";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void button7(View v) {

        /* Add the digit to the expression. */
        expression += "7";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void button8(View v) {

        /* Add the digit to the expression. */
        expression += "8";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void button9(View v) {

        /* Add the digit to the expression. */
        expression += "9";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void buttonA(View v) {

        /* Add the digit to the expression. */
        expression += "A";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void buttonB(View v) {

        /* Add the digit to the expression. */
        expression += "B";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void buttonC(View v) {

        /* Add the digit to the expression. */
        expression += "C";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void buttonD(View v) {

        /* Add the digit to the expression. */
        expression += "D";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void buttonE(View v) {

        /* Add the digit to the expression. */
        expression += "E";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }

    public void buttonF(View v) {

        /* Add the digit to the expression. */
        expression += "F";

        /* Update the screen. */
        TextView expressionText = (TextView) findViewById(R.id.expressionText);
        expressionText.setText(expression);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /* Menu to switch between the calculator and converter. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i;

        switch(item.getItemId()) {

            /* Get the selected item id (currently only contains calculator and converter). */
            case R.id.menu_converter:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                /* Instantiate intent for the base converter and start the intent. */
                i = new Intent(this, BaseConverter.class);
                startActivity(i);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
