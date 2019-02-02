package com.msaini.cafetriamanagementsystem;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

public class RecycleViewVendorOrder extends RecyclerView.Adapter<RecycleViewVendorOrder.MyViewHolder> {
    ArrayList<FinalOrder> orderListNew;
    FinalOrder list;
    TaskRecycler taskRecycler;
    DatabaseReference databaseReference;
    PrettyTime p;
    public View v;

    public RecycleViewVendorOrder(ArrayList<FinalOrder> messageList, TaskRecycler taskRecycler) {
        this.orderListNew = messageList;
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        this.taskRecycler = taskRecycler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_vednor_custom_order, parent, false);
        this.v = v;
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        list = orderListNew.get(position);
        p = new PrettyTime();

        holder.OrderDesc.setText("Order Number "+list.orderNumber);
        //holder.orderUser.setText(list.getUser());
        holder.orderQuantity.setText(list.userName);
        //String foodListName = list.foodItems;

        String data = list.foodItems;
        String[] items = data.split(",");
        StringBuilder foodData = new StringBuilder();

        String dataQuant = list.foodQuant;
        String[] itemsQuant = dataQuant.split(",");
       // StringBuilder foodQunat = new StringBuilder();

        for (int i = 0; i < items.length && i < items.length; i++) {
           // holder.foodItems.setText(items[i]);
            foodData.append(items[i]+" - "+itemsQuant[i] + "\n");
        }

        /*for (int i = 0; i < itemsQuant.length && i < itemsQuant.length; i++) {
            // holder.foodItems.setText(items[i]);
            foodQunat.append(itemsQuant[i] + "\n");
        }
*/
        holder.foodItems.setText(foodData.toString());
       // holder.foodQunat.setText(foodQunat.toString());


       /* switch (TASK.priority){
            case 3: holder.taskPriority.setText("High"); break;
            case 2: holder.taskPriority.setText("Medium"); break;
            case 1: holder.taskPriority.setText("Low"); break;
        }*/

       holder.orderDate.setText(p.format(new Date(list.scheduleTime)));
       holder.isDoneCheckBox.setChecked(list.isCompleted);

    }

    @Override
    public int getItemCount() {
        return orderListNew.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView OrderDesc;
        TextView orderUser;
        TextView orderDate;
        TextView orderQuantity;
        CheckBox isDoneCheckBox;
        VendorDetails vedndorDetails;
        TextView foodItems;
        TextView foodQunat;

        public MyViewHolder(View v) {
            super(v);
            this.OrderDesc = v.findViewById(R.id.foodDesc);
            this.orderUser = v.findViewById(R.id.orderUserText);
            this.isDoneCheckBox = (CheckBox) v.findViewById(R.id.doneCheckBox);
            this.orderDate = v.findViewById(R.id.taskDate);
            this.orderQuantity = v.findViewById(R.id.orderQuantity);
            this.foodItems = v.findViewById(R.id.foodItems);
            //this.foodQunat = v.findViewById(R.id.foodQ);


            ((CheckBox) v.findViewById(R.id.doneCheckBox)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderListNew.get(getPosition()).isCompleted = !orderListNew.get(getPosition()).isCompleted;
                    taskRecycler.updateIsDone(orderListNew.get(getPosition()));
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    taskRecycler.deleteTask(orderListNew.get(getPosition()));
                    return false;
                }
            });

        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("demo", "long press");
            return false;
        }
    }


    public interface TaskRecycler {
        void updateIsDone(FinalOrder task);

        void deleteTask(FinalOrder taskData);

        int showOption();
    }


}