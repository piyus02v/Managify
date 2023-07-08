package com.example.managify.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.managify.R
import com.example.managify.adapters.CardMemberListItemAdapter
import com.example.managify.dialogs.LabelColorListDialog
import com.example.managify.dialogs.MembersListDialog
import com.example.managify.firebase.FirestoreHandler
import com.example.managify.models.*
import com.example.managify.utils.Constants
import java.util.*
import kotlin.collections.ArrayList

class CardDetailsActivity : BaseActivity() {
    private lateinit var mBoardDetails: Board
    private lateinit var mMemberDetailList: ArrayList<User>
    private var mTaskListPosition = -1
    private var mCardListPosition = -1
    private var mSelectedColor = ""
    private var mSelectedDueDateMS : Long = 0 //store the date as milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)
        getIntentData()

        setActionBar()

        findViewById<AppCompatEditText>(R.id.name_card_details_et).setText(mBoardDetails.taskList[mTaskListPosition].cards[mCardListPosition].name)
        findViewById<AppCompatEditText>(R.id.name_card_details_et).setSelection(findViewById<AppCompatEditText>(R.id.name_card_details_et).text.toString().length) //set focus in the end of the text

        mSelectedColor =
            mBoardDetails.taskList[mTaskListPosition].cards[mCardListPosition].labelColor
        if (mSelectedColor.isNotEmpty()) {
            setColor()
        }

        findViewById<Button>(R.id.update_card_details_btn).setOnClickListener {
            if (findViewById<AppCompatEditText>(R.id.name_card_details_et).text.toString().isNotEmpty()) {
                updateCardDetails()
            } else {
                Toast.makeText(this, "Please enter a card name", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.select_label_color_tv).setOnClickListener {
            labelColorsListDialog()
        }

        findViewById<TextView>(R.id.select_members_tv).setOnClickListener {
            membersListDialog()
        }

        mSelectedDueDateMS = mBoardDetails.taskList[mTaskListPosition].cards[mCardListPosition].dueDate

        //set selected date if its not default value
        if(mSelectedDueDateMS > 0){
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val selectedDate = simpleDateFormat.format(Date(mSelectedDueDateMS))
            findViewById<TextView>(R.id.select_due_date_tv).text = selectedDate
        }

        findViewById<TextView>(R.id.select_due_date_tv).setOnClickListener {
            showDatePicker()
        }

        setupSelectedMembersList()
    }



    private fun setActionBar() {
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar_card_details_activity))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white)
            actionBar.title =
                mBoardDetails.taskList[mTaskListPosition].cards[mCardListPosition].name
        }

        findViewById<Toolbar>(R.id.toolbar_card_details_activity).setNavigationOnClickListener { onBackPressed() }
    }

    private fun getIntentData() {

        if (intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)) {
            mTaskListPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION, -1)
        }
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)) {
            mCardListPosition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION, -1)
        }
        if (intent.hasExtra(Constants.BOARD_DETAIL)) {
            mBoardDetails = intent.getParcelableExtra<Board>(Constants.BOARD_DETAIL)!! as Board
        }
        if (intent.hasExtra(Constants.BOARD_MEMBERS_LIST)) {
            mMemberDetailList = intent.getParcelableArrayListExtra(Constants.BOARD_MEMBERS_LIST)!!
        }
    }

    fun addUpdateTaskListSuccess() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun updateCardDetails() {
        val card = Card(
            findViewById<AppCompatEditText>(R.id.name_card_details_et).text.toString(),
            mBoardDetails.taskList[mTaskListPosition].cards[mCardListPosition].createdBy,
            mBoardDetails.taskList[mTaskListPosition].cards[mCardListPosition].assignedTo,
            mSelectedColor,
            mSelectedDueDateMS
        )

        val taskList : ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size-1)

        mBoardDetails.taskList[mTaskListPosition].cards[mCardListPosition] = card

        showProgressDialog()
        FirestoreHandler().addUpdateTaskList(this@CardDetailsActivity, mBoardDetails)
    }

    private fun membersListDialog() {
        var cardAssignedMembersList =
            mBoardDetails.taskList[mTaskListPosition].cards[mCardListPosition].assignedTo

        if (cardAssignedMembersList.size > 0) { //check if we have any members in the list
            for (i in mMemberDetailList.indices) { //go through all the member assigned to the board and the card and check
                for (j in cardAssignedMembersList) {
                    if (mMemberDetailList[i].id == j) { //if they are the same set check sign
                        mMemberDetailList[i].selected = true
                    }
                }
            }
        }else{
            for(i in mMemberDetailList.indices){
                mMemberDetailList[i].selected = false
            }
        }
        //create dialog with members and show it
        val listDialog = object : MembersListDialog(
            this,
            mMemberDetailList,
            "Select Members"
        ){
            override fun onItemSelected(user: User, action: String) {
                if(action == Constants.SELECT){
                    if(!mBoardDetails
                            .taskList[mTaskListPosition]
                            .cards[mCardListPosition]
                            .assignedTo.contains(user.id)){
                        mBoardDetails
                            .taskList[mTaskListPosition]
                            .cards[mCardListPosition]
                            .assignedTo.add(user.id)
                    }
                }else{
                    mBoardDetails
                        .taskList[mTaskListPosition]
                        .cards[mCardListPosition]
                        .assignedTo.remove(user.id)

                    for(i in mMemberDetailList.indices){
                        if(mMemberDetailList[i].id == user.id){
                            mMemberDetailList[i].selected = false
                        }
                    }
                }
                setupSelectedMembersList()
            }
        }
        listDialog.show()
    }

    private fun deleteCard() {
        val cardsList: ArrayList<Card> = mBoardDetails.taskList[mTaskListPosition].cards
        cardsList.removeAt(mCardListPosition)

        val tasksList: ArrayList<Task> = mBoardDetails.taskList
        tasksList.removeAt(tasksList.size - 1)

        tasksList[mTaskListPosition].cards = cardsList

        showProgressDialog()
        FirestoreHandler().addUpdateTaskList(this, mBoardDetails)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_card -> {
                val cardTitle =
                    mBoardDetails.taskList[mTaskListPosition].cards[mCardListPosition].name
                alertDialogForDeleteCard(cardTitle)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun alertDialogForDeleteCard(cardName: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Alert!")
        builder.setMessage("Are you sure you want to delete $cardName?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            deleteCard()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun colorsList(): ArrayList<String> {
        val colorsList: ArrayList<String> = ArrayList()
        colorsList.add("#0C90F1")
        colorsList.add("#F72400")
        colorsList.add("#90F700")
        colorsList.add("#8400F7")
        colorsList.add("#F700CE")
        colorsList.add("#FF9800")
        colorsList.add("#7A8089")

        return colorsList
    }

    private fun setColor() {
        findViewById<TextView>(R.id.select_label_color_tv).text = ""
        findViewById<TextView>(R.id.select_label_color_tv).setBackgroundColor(Color.parseColor(mSelectedColor))
    }

    private fun labelColorsListDialog() {
        val colorsList: ArrayList<String> = colorsList()
        val listDialog = object : LabelColorListDialog(
            this,
            colorsList,
            "Select Label Color",
            mSelectedColor
        ) {
            override fun onItemSelected(color: String) {
                mSelectedColor = color
                setColor()
            }
        }
        listDialog.show()
    }

    private fun setupSelectedMembersList(){
        val cardAssignedMembersList = mBoardDetails.taskList[mTaskListPosition].cards[mCardListPosition].assignedTo
        val selectedMembersList : ArrayList<SelectedMembers> = ArrayList()

        for (i in mMemberDetailList.indices) { //go through all the member assigned to the board and the card and check
            for (j in cardAssignedMembersList) {
                if (mMemberDetailList[i].id == j) { //if they are the same set check sign
                    val selectedMember = SelectedMembers(mMemberDetailList[i].id, mMemberDetailList[i].image)
                    selectedMembersList.add(selectedMember)
                }
            }
        }
        if (selectedMembersList.size > 0){
            selectedMembersList.add(SelectedMembers("",""))
            findViewById<TextView>(R.id.select_members_tv).visibility = View.GONE
            findViewById<RecyclerView>(R.id.selected_members_list_rv).visibility = View.VISIBLE

            findViewById<RecyclerView>(R.id.selected_members_list_rv).layoutManager = GridLayoutManager(
                this,6
            )

            val adapter = CardMemberListItemAdapter(this,selectedMembersList, true)
            findViewById<RecyclerView>(R.id.selected_members_list_rv).adapter = adapter
            adapter.setOnClickListener(
                object : CardMemberListItemAdapter.OnClickListener{
                    override fun onClick() {
                        membersListDialog()
                    }
                }
            )
        }else{
            findViewById<TextView>(R.id.select_members_tv).visibility = View.VISIBLE
            findViewById<RecyclerView>(R.id.selected_members_list_rv).visibility = View.GONE
        }
    }

    private fun showDatePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialogListener =  DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            val sMonthOfYear = if((monthOfYear + 1) < 10) "0${monthOfYear+1}" else "${monthOfYear+1}"

            val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
            findViewById<TextView>(R.id.select_due_date_tv).text = selectedDate

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val theDate = sdf.parse(selectedDate)
            mSelectedDueDateMS = theDate!!.time
        }

        val datePickerDialog = DatePickerDialog(
            this,
            datePickerDialogListener,
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}