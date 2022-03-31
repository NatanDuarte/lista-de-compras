package com.natanduarte.listadecompras.ui.activity;

import static com.natanduarte.listadecompras.ui.activity.ConstantsActivities.APP_TITLE_NEW_ITEM;
import static com.natanduarte.listadecompras.ui.activity.ConstantsActivities.APP_TITLE_UPDATE;
import static com.natanduarte.listadecompras.ui.activity.ConstantsActivities.PRODUCT_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.natanduarte.listadecompras.R;
import com.natanduarte.listadecompras.database.ProductDatabase;
import com.natanduarte.listadecompras.database.dao.ProductDAO;
import com.natanduarte.listadecompras.model.Product;

public class FormNewItem extends AppCompatActivity {

    private EditText nameField;
    private EditText quantityField;
    private Product product;
    private ProductDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_item);
        dao = ProductDatabase.getInstance(this)
                .getRoomProductDAO();
        initializeFields();
        initializeProduct();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.activity_form_new_item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.activity_form_new_item_menu_save)
            handleSaveButton();
        return super.onOptionsItemSelected(item);
    }

    private void initializeProduct() {
        Intent intent = getIntent();

        if (intent.hasExtra(PRODUCT_KEY)) {
            setTitle(APP_TITLE_UPDATE);
            instantiateProductFromExtra(intent);
        } else {
            setTitle(APP_TITLE_NEW_ITEM);
            product = new Product();
        }
    }

    @SuppressLint("DefaultLocale")
    private void instantiateProductFromExtra(@NonNull Intent intent) {
        product = (Product) intent.getSerializableExtra(PRODUCT_KEY);
        nameField.setText(product.getName());
        quantityField.setText(String.format("%d", product.getQuantity()));
    }

    private void initializeFields() {
        nameField = findViewById(R.id.activity_form_new_item_name);
        quantityField = findViewById(R.id.activity_form_new_item_quantity);
    }

    private void handleSaveButton() {
        if (fieldsAreInvalid())
            return;

        populateFields();
        evaluateDaoAction();
        finish();
    }

    private void evaluateDaoAction() {
        if (product.hasValidId())
            dao.update(product);
        else
            dao.save(product);
    }

    private void populateFields() {
        String name = nameField.getText().toString();
        int quantity = Integer.parseInt(quantityField.getText().toString());
        product.setName(name);
        product.setQuantity(quantity);
    }

    private boolean fieldsAreInvalid() {
        String name = nameField.getText().toString();
        int quantity = Integer.parseInt(quantityField.getText().toString());

        boolean quantityNotValid = quantity <= 0;
        boolean fieldsAreEmpty = name.isEmpty() ||
                quantityField.getText().toString().isEmpty();

        if (fieldsAreEmpty || quantityNotValid) {
            final String toastMessage = "Preencha os campos corretamente";

            Toast.makeText(this, toastMessage,
                    Toast.LENGTH_SHORT).show();

            return true;
        }
        return false;
    }
}