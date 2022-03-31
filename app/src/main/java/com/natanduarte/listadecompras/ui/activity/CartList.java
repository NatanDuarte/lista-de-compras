package com.natanduarte.listadecompras.ui.activity;

import static com.natanduarte.listadecompras.ui.activity.ConstantsActivities.PRODUCT_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.natanduarte.listadecompras.R;
import com.natanduarte.listadecompras.database.ProductDatabase;
import com.natanduarte.listadecompras.database.dao.ProductDAO;
import com.natanduarte.listadecompras.model.Product;


import java.util.List;

public class CartList extends AppCompatActivity {

    private static final String TITLE = "Lista de compras";
    private ArrayAdapter<Product> adapter;
    private ProductDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setTitle(TITLE);

        dao = ProductDatabase.getInstance(this)
                .getRoomProductDAO();

        handleFabNewItem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.activity_cart_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.activity_cart_list_option_send)
            sendList();
        if (itemId == R.id.activity_cart_list_option_clear)
            clearList();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleListView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_cart_products_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.activity_cart_products_menu_remove) {
            AdapterView.AdapterContextMenuInfo menuInfo =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Product product = adapter.getItem(menuInfo.position);
            dao.delete(product);
            adapter.remove(product);
        }
        return super.onContextItemSelected(item);
    }

    private void clearList() {
        if (noItemsToSend(dao.selectAll(),
                "A lista já está vazia.")) return;

        dao.clear();
        adapter.clear();
    }

    private void sendList() {
        List<Product> products = dao.selectAll();

        if (noItemsToSend(
                products,
                "Não há itens para enviar")) return;

        String regex = "\\[|,.|]";
        String[] split = products.toString().split(regex);
        String joined = String.join("\n", split);
        String result = String.format("Compras:%s", joined);
        Share(result);
    }

    private boolean noItemsToSend(List<Product> products, String message) {
        boolean noItems = products.size() <= 0;
        if (noItems)
            Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_SHORT
            ).show();
        return noItems;
    }

    private void Share(String stringFromList) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, stringFromList);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    private void handleListView() {
        ListView cartListView = findViewById(R.id.activity_cart_list_of_product);
        final List<Product> products = dao.selectAll();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                products
        );
        cartListView.setAdapter(adapter);

        cartListView.setOnItemClickListener(
                (adapterView, view, position, id) ->
                        openUpdateForm(adapterView, position));

        registerForContextMenu(cartListView);
    }

    private void openUpdateForm(AdapterView<?> adapterView, int position) {
        Product chosenProduct =
                (Product) adapterView.getItemAtPosition(position);
        Intent navigateToFormNewItemActivity =
                new Intent(CartList.this, FormNewItem.class);

        navigateToFormNewItemActivity.putExtra(PRODUCT_KEY, chosenProduct);
        startActivity(navigateToFormNewItemActivity);
    }

    private void handleFabNewItem() {
        FloatingActionButton fabNewItem =
                findViewById(R.id.activity_cart_list_fab_new_item);
        fabNewItem.setOnClickListener(view -> openFormView());
    }

    private void openFormView() {
        Intent intent = new Intent(this, FormNewItem.class);
        startActivity(intent);
    }
}
