package com.example.fooddelivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MenuFragment extends Fragment implements SearchView.OnQueryTextListener{

    private EditText etSearch;
    private ImageButton catMakanan,catMinuman,catDesert,catAll;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<DataMenu> menuList;
    private ImageView imgSearch,imgCancel; // tinggal diganti ke MenuItem kalau ga salah tehe :c
    private MenuAdapter adapter;
    private TextView addmenu;
    private FrameLayout layout;
    private boolean aBoolean;
    private String kategori = "";
    private Animation rightin,rightout,leftin,leftout;
    private TableLayout tableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // dont u dare to put anything in this field
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch = view.findViewById(R.id.search_bar);
        catMakanan = view.findViewById(R.id.Kategori_Makanan);
        catMinuman = view.findViewById(R.id.Kategori_Minuman);
        catDesert = view.findViewById(R.id.Kategori_Desert);
        catAll = view.findViewById(R.id.Kategori_All);
        addmenu = view.findViewById(R.id.addData);
        imgSearch = view.findViewById(R.id.iconSearch);
        tableLayout = view.findViewById(R.id.categorycontainer);
        imgCancel = view.findViewById(R.id.cancel_button);



        databaseReference = FirebaseDatabase.getInstance().getReference("data-barang");
        catAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addmenu.setText("All");
            showAllMenu();
            }
        });

        catDesert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategori = "Desert";
                searchKategori(kategori);
            }
        });

        catMinuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategori = "Minuman";
                searchKategori(kategori);
            }
        });

        catMakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategori = "Makanan";
                searchKategori(kategori);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchMenu(etSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                SearchMenu(etSearch.getText().toString());
            }
        });

// JANGAN DIHAPUS BUAT GALIH WKWKWK
//        imgSearch = view.findViewById(R.id.iconsearch);
//        imgSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                etSearch.setText("");
//                etSearch.setVisibility(View.VISIBLE);
//                imgSearch.setVisibility(View.INVISIBLE);
//                etSearch.requestFocus();
//                etSearch.setFocusableInTouchMode(true);
//                InputMethodManager imp = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//                imp.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCancel.setVisibility(View.INVISIBLE);
                etSearch.setVisibility(View.INVISIBLE);
                imgSearch.setVisibility(View.VISIBLE);
                rightin = AnimationUtils.loadAnimation(getContext() ,R.anim.push_right_in);
                rightout = AnimationUtils.loadAnimation(getContext(), R.anim.push_right_out);
                etSearch.setAnimation(rightout);
                tableLayout.setAnimation(rightin);
                tableLayout.setVisibility(View.VISIBLE);
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCancel.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.INVISIBLE);
                leftin = AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in);
                leftout = AnimationUtils.loadAnimation(getContext(), R.anim.push_left_out);
                tableLayout.setAnimation(leftout);
                etSearch.setAnimation(leftin);
                etSearch.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.INVISIBLE);
                etSearch.requestFocus();
            }
        });

        addmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddMenu.class);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        menuList = new ArrayList<>();
        showAllMenu();
    }


    @Override
    public void onResume() {
        super.onResume();
        menuList.clear();
    }

    private void SearchMenu(String search){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuList.clear();
                for(DataSnapshot item : snapshot.getChildren()) {
                    DataMenu dataMenu = item.getValue(DataMenu.class);
                    if(dataMenu.getNama().toLowerCase().contains(search.toLowerCase())) {
                        menuList.add(dataMenu);
                    }
                }
                viewMenu(menuList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchKategori(String kategori) {
        addmenu.setText(kategori);
        menuList.clear();
        databaseReference.orderByChild("kategori").equalTo(kategori).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    DataMenu dataMenu = item.getValue(DataMenu.class);
                    menuList.add(dataMenu);
                }
                viewMenu(menuList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showAllMenu() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    DataMenu dataMenu = item.getValue(DataMenu.class);
                    menuList.add(dataMenu);
                }
                viewMenu(menuList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void viewMenu(List list) {
        adapter = new MenuAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }
}