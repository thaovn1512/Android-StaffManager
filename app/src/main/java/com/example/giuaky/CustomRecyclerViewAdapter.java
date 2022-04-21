package com.example.giuaky;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<StaffViewHolder> {

    private ArrayList<Staff> staffs;
    private Context context;
    private LayoutInflater mLayoutInflater;
    private Uri uri;
    private StaffLongClickItemListener listener = null;

    private boolean isAllowLongClick = true;

    public CustomRecyclerViewAdapter(Context context, ArrayList<Staff> staffs, StaffLongClickItemListener listener) {
        this.context = context;
        this.staffs = staffs;
        this.mLayoutInflater = LayoutInflater.from(context);

        this.listener = listener;
    }

    public CustomRecyclerViewAdapter(Context context, ArrayList<Staff> staffs, boolean isAllowLongClick) {
        this.context = context;
        this.staffs = staffs;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.isAllowLongClick = isAllowLongClick;
    }

    public void setNewData(ArrayList<Staff> staffs) {
        this.staffs = staffs;
    }


    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View recyclerViewItem = mLayoutInflater.inflate(R.layout.staff_item, parent, false);

        recyclerViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRecyclerItemClick((RecyclerView) parent, view);
            }
        });

        if (isAllowLongClick)
            recyclerViewItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    BottomSheet bottomSheet = new BottomSheet(staffs.get(((RecyclerView) parent).getChildLayoutPosition(view)).getId(), listener);
                    bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "ModalBottomSheet");
                    return true;
                }
            });

        return new StaffViewHolder(recyclerViewItem);
    }


    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {

        Staff staff = this.staffs.get(position);

        holder.txtStaffName.setText(staff.getfName() + " " + staff.getlName());
        holder.txtIDStaff.setText(staff.getId());
        holder.txtFactory.setText("Phân xưởng " + staff.getFactory());
        holder.txtDay.setText("Ngày công: " + staff.getTmpSumDay());
//        uri= Uri.parse(staff.getImage());
       // Toast.makeText(context, "Uri: "+uri, Toast.LENGTH_SHORT).show();

        //File f=new File(staff.getImage(), staff.getId()+".jpg");
        File f= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            f = new File(staff.getImage());
        }
        Uri uri = null;
        uri = Uri.fromFile(f);
      //  holder.imgStaffAva.setImageBitmap(b);
        Transformation transformation = new RoundedTransformationBuilder()
                .oval(true)
                .build();
        Picasso.get()
                .load(uri)
                .transform(transformation)
                .into(holder.imgStaffAva);
    }

    @Override
    public int getItemCount() {
        return this.staffs.size();
    }

    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
        Staff staff = this.staffs.get(itemPosition);

        Intent intent = new Intent(itemView.getContext(), Details.class);

        intent.putExtra("staffID", staff.getId());

        itemView.getContext().startActivity(intent);
    }
}
