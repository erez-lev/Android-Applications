package com.erezlev.calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

// Udemy solution:
// private const val STATE_PENDING_OPERATION = "PendingOperation"
// private const val STATE_OPERAND! = "Operand1"
// private const val STATE_OPERAND1-STORED = "operan1_Stored"

// My solution:
//private const val RESULT = "Result"
//private const val PENDING = "Pending"
//private const val OPERAND1 = "Operator1"
//private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    // Properties:
//    private lateinit var result: EditText
//    private lateinit var newNumber: EditText
//    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    // Methods:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: BigDecimalViewModel by viewModels()
        viewModel.stringResult.observe(this, Observer<String> { stringResult -> result.setText(stringResult)})
        viewModel.stringNewNumber.observe(this, Observer<String> { stringNumber -> newNumber.setText(stringNumber)})
        viewModel.stringOperation.observe(this, Observer<String> { stringOperation -> operation.text = stringOperation })

//        result = findViewById(R.id.result)
//        newNumber = findViewById(R.id.newNumber)
//
//        // Data input buttons
//        val button0: Button = findViewById(R.id.button0)
//        val button1: Button = findViewById(R.id.button1)
//        val button2: Button = findViewById(R.id.button2)
//        val button3: Button = findViewById(R.id.button3)
//        val button4: Button = findViewById(R.id.button4)
//        val button5: Button = findViewById(R.id.button5)
//        val button6: Button = findViewById(R.id.button6)
//        val button7: Button = findViewById(R.id.button7)
//        val button8: Button = findViewById(R.id.button8)
//        val button9: Button = findViewById(R.id.button9)
//        val buttonDot: Button = findViewById(R.id.buttonDot)
//
//        // Operation buttons
//        val buttonEquals = findViewById<Button>(R.id.buttonEquals)
//        val buttonDivide = findViewById<Button>(R.id.buttonDivide)
//        val buttonMulti = findViewById<Button>(R.id.buttonMulti)
//        val buttonMinus = findViewById<Button>(R.id.buttonMinus)
//        val buttonPlus = findViewById<Button>(R.id.buttonPlus)

        val listener = View.OnClickListener { v ->
            viewModel.digitPressed((v as Button).text.toString())
        }

        // Set all 0-9 buttons to the setOnClickListener methods, for allowing the click able buttons.
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        // Creating a listener for the operations button.
        val opListener = View.OnClickListener { v ->
            viewModel.operandPressed((v as Button).text.toString())
        }

        // Set the operations button to be touch able.
        buttonEquals.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonMulti.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)

        // Listener for the negetive button
        val negOperator = View.OnClickListener {
            viewModel.negPressed()
        }

        // Set the negative button to be touchable.
        buttonNeg.setOnClickListener(negOperator)

        // A listener for the clear button.
        buttonClr.setOnClickListener {
            viewModel.clearPressed()
        }

    }




//    override fun onSaveInstanceState(outState: Bundle) {
//        // Log.d(TAG, "onSaveInstanceState: called")
//        super.onSaveInstanceState(outState)
//
//        // Udemy solution:
////        if(operand1 != null) {
////            outState.putDouble(STATE_OPERAND1, operand1)
////        outState.putBoolean(STATE_OPERAND1_STORED, true)
////        }
////        outState.putString(STATE_PENDING_OPERTION, pendingOperations)
//
//        // My solution:
//        // Log.d(TAG, "OnSave: ${result.text}" )
//        outState.putString(RESULT, result.text.toString())
//        outState.putString(PENDING, pendingOperations)
//        if (operand1 != null)
//            outState.putDouble(OPERAND1, operand1!!)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        Log.d(TAG, "onRestoreInstanceState: called")
//        super.onRestoreInstanceState(savedInstanceState)
//
//        // Udemy solution
////        if(savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)) {
////                    operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
////
////        } else {
////            null
////        }
////
////        pendingOperations = savedInstanceState.getString(STATE_PENDING_OPERATION)
////        displayOperation.text = pendingOperations
//
//        // My solution
//
//        // Log.d(TAG, "OnRestore: ${result.text}" )
//        // Log.d(TAG, "OnRestore: $operand1" )
//
//        operand1 = savedInstanceState.getDouble(OPERAND1)
//        if (operand1 == null) {
//            result.setText(savedInstanceState.getString(RESULT))
//        }
//        pendingOperations = savedInstanceState.getString(PENDING)!!
//        if (pendingOperations != "=") {
//            operation.text = pendingOperations
//        }
//    }
}
