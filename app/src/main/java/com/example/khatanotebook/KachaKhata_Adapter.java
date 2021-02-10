package com.example.khatanotebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class KachaKhata_Adapter extends FirebaseRecyclerAdapter<KachaKhata_ModelClass, KachaKhata_Adapter.Manager_ViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public KachaKhata_Adapter(@NonNull FirebaseRecyclerOptions<KachaKhata_ModelClass> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Manager_ViewHolder holder, int position, @NonNull KachaKhata_ModelClass model) {
        holder.kk_tvName.setText(model.getKhata_Name());
        holder.kk_tvDate.setText(model.getDateTime());
        holder.kk_tvProduct.setText(model.getProduct_Name());
        holder.kk_tvTotalBill.setText("Total Bill = "+model.getTotal_Bill());
        holder.kk_Advance.setText("Advance Bill = "+model.getAdvance());
//        holder.kk_Pending.setText("Pending Bill = "+model.getPending());
        holder.kk_PhoneNumber.setText("Phone no # "+model.getPhone_Number());
        int Pending = Integer.parseInt(model.getTotal_Bill())-Integer.parseInt(model.getAdvance());
        holder.kk_Pending.setText("Pending Bill = "+Integer.toString(Pending));
        holder.kk_ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("KachaKhata").child(getRef(position).getKey())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context,"Bill Deleted",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.kk_ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.kk_editkhata_dialog))
                        .setGravity(Gravity.CENTER)
                        .setMargin(50,0,50,0)
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();
                View view = dialog.getHolderView();
                EditText kk_ProductDialog, kk_TotalBillDialog, kk_AdvanceBillDialog, kk_PhoneNumberDialog;
                Button kk_btnUpdateDialog;
                kk_ProductDialog = view.findViewById(R.id.kk_ProductDialog);
                kk_TotalBillDialog = view.findViewById(R.id.kk_TotalBillDialog);
                kk_AdvanceBillDialog = view.findViewById(R.id.kk_AdvanceBillDialog);
                kk_PhoneNumberDialog = view.findViewById(R.id.kk_PhoneNumberDialog);
                kk_btnUpdateDialog = view.findViewById(R.id.kk_btnUpdateDialog);
                kk_ProductDialog.setText(model.getProduct_Name());
                kk_TotalBillDialog.setText(model.getTotal_Bill());
                kk_AdvanceBillDialog.setText(model.getAdvance());
                kk_PhoneNumberDialog.setText(model.getPhone_Number());
                kk_btnUpdateDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> data_a = new HashMap<>();
                        data_a.put("Product_Name",kk_ProductDialog.getText().toString());
                        data_a.put("Total_Bill",kk_TotalBillDialog.getText().toString());
                        data_a.put("Advance",kk_AdvanceBillDialog.getText().toString());
                        data_a.put("Phone_Number",kk_PhoneNumberDialog.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("KachaKhata").child(getRef(position).getKey())
                                .updateChildren(data_a).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context,"Data Updated Successfully",Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });
            }
        });
        holder.kk_ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+model.getPhone_Number()));
                context.startActivity(intent);
            }
        });
        holder.kk_ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smgr = SmsManager.getDefault();
                String message ="Thanks for Shopping"+"\n\n"+model.getProduct_Name()+"\n"+"Date : "+model.getDateTime()+"\n"+"Total Bill = "+model.getTotal_Bill()+"\n"+
                        "Advance = "+model.getAdvance()+"\n"+"Pending Bill = "+Integer.toString(Pending)+"\n\n"+"Regards : Abid General Store";
                smgr.sendTextMessage(model.getPhone_Number(),null,message,null,null);
                Toast.makeText(context,"Message Sent",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public Manager_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kachakhata_recyclerview, parent, false);

        return new Manager_ViewHolder(view);
    }

    public static class Manager_ViewHolder extends RecyclerView.ViewHolder{
        TextView kk_tvName, kk_tvDate, kk_tvProduct, kk_tvTotalBill, kk_Advance, kk_Pending, kk_PhoneNumber;
        ImageView kk_ivDelete, kk_ivEdit, kk_ivCall, kk_ivMessage;
        public Manager_ViewHolder(@NonNull View itemView) {
            super(itemView);
            kk_tvName = itemView.findViewById(R.id.kk_tvName);
            kk_tvDate = itemView.findViewById(R.id.kk_tvDate);
            kk_tvProduct = itemView.findViewById(R.id.kk_tvProduct);
            kk_tvTotalBill = itemView.findViewById(R.id.kk_tvTotalBill);
            kk_Advance = itemView.findViewById(R.id.kk_Advance);
            kk_Pending = itemView.findViewById(R.id.kk_Pending);
            kk_PhoneNumber = itemView.findViewById(R.id.kk_PhoneNumber);
            kk_ivDelete = itemView.findViewById(R.id.kk_ivDelete);
            kk_ivEdit = itemView.findViewById(R.id.kk_ivEdit);
            kk_ivCall = itemView.findViewById(R.id.kk_ivCall);
            kk_ivMessage = itemView.findViewById(R.id.kk_ivMessage);

        }
    }
}
