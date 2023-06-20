package com.example.freeapp_curvefever.Menu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.freeapp_curvefever.Game.MainActivity
import com.example.freeapp_curvefever.R
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder


class MenuActivity : AppCompatActivity(), MenuView {
    lateinit var playerColorPicker : Button
    lateinit var playerColorViewer : View
    lateinit var minusPlayerShipButton : Button
    lateinit var plusPlayerShipButton : Button
    lateinit var playerShipImageView : ImageView
    lateinit var nPlayersEditText : EditText
    lateinit var nRoundsEditText : EditText
    lateinit var playButton : Button
    lateinit var jumpCheckBox : CheckBox
    lateinit var thickDownCheckBox : CheckBox
    lateinit var thickUpCheckBox : CheckBox
    lateinit var speedUpCheckBox : CheckBox
    lateinit var soundSwitch : Switch

    val model : MenuModel = MenuModel()
    val presenter : MenuPresenter = MenuPresenter(this, model)

    val shipImageResources = arrayListOf(
        R.drawable.ship_1_icon,
        R.drawable.ship_2_icon,
        R.drawable.ship_3_icon,
        R.drawable.ship_4_icon,
        R.drawable.ship_5_icon,
        R.drawable.ship_6_icon
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        playerColorPicker = findViewById(R.id.playerColorPicker)
        playerColorPicker.setOnClickListener { presenter.colorPickerPressed() }
        playerColorViewer = findViewById(R.id.playerColorViewer)
        minusPlayerShipButton = findViewById(R.id.minusPlayerShipButton)
        minusPlayerShipButton.setOnClickListener { presenter.changePlayerShipIndex(-1) }
        plusPlayerShipButton = findViewById(R.id.plusPlayerShipButton)
        plusPlayerShipButton.setOnClickListener { presenter.changePlayerShipIndex(1) }
        playerShipImageView = findViewById(R.id.playerShipImageView)
        nPlayersEditText = findViewById(R.id.nPlayersEditText)
        nPlayersEditText.addTextChangedListener { presenter.checkInputs(nPlayersEditText.text.toString(), nRoundsEditText.text.toString()) }
        nRoundsEditText = findViewById(R.id.nRoundsEditText)
        nRoundsEditText.addTextChangedListener { presenter.checkInputs(nPlayersEditText.text.toString(), nRoundsEditText.text.toString()) }
        jumpCheckBox = findViewById(R.id.jumpCheckBox)
        speedUpCheckBox = findViewById(R.id.speedUpCheckBox)
        thickDownCheckBox = findViewById(R.id.sizeDownCheckBox)
        thickUpCheckBox = findViewById(R.id.thickUpCheckBox)
        playButton = findViewById(R.id.playButton)
        playButton.setOnClickListener { presenter.playButtonPressed(
            speedUpCheckBox.isChecked,
            thickUpCheckBox.isChecked,
            thickDownCheckBox.isChecked,
            jumpCheckBox.isChecked,
            soundSwitch.isChecked
        ) }
        soundSwitch = findViewById(R.id.soundSwitch)
        if(savedInstanceState != null){
            presenter.setState(savedInstanceState)
        }
    }

    override fun changePlayerShipImage(index: Int) {
        playerShipImageView.setImageResource(shipImageResources[index-1])
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }
    override fun invalidInput(message : String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
        playButton.isEnabled = false
    }

    override fun validInput(){
        playButton.isEnabled = true
    }

    override fun showPlayerColorPickerDialog() {
        val initialColor = Color.WHITE

        ColorPickerDialogBuilder
            .with(this)
            .setTitle("Choose color")
            .initialColor(initialColor)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorSelectedListener { selectedColor ->
                Toast.makeText(
                    this,
                    "onColorSelected: 0x" + Integer.toHexString(
                        selectedColor
                    ),
                    Toast.LENGTH_LONG
                ).show()
            }
            .setPositiveButton(
                "ok"
            ) { dialog, selectedColor, allColors ->
                presenter.playerColorPicked(selectedColor)
                playerColorViewer.setBackgroundColor(selectedColor)
            }
            .setNegativeButton(
                "cancel"
            ) { dialog, which -> }
            .build()
            .show()
    }

    override fun startGameActivity(gameInfo: GameInfo) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MainActivity.GAME_INFO, gameInfo)
        startActivity(intent)
        this.finish()
    }
}