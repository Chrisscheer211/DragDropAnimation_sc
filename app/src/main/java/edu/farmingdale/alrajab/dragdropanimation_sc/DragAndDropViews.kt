package edu.farmingdale.alrajab.dragdropanimation_sc

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import edu.farmingdale.alrajab.dragdropanimation_sc.databinding.ActivityDragAndDropViewsBinding

class DragAndDropViews : AppCompatActivity() {

    // Create binding for activity
    lateinit var binding: ActivityDragAndDropViewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDragAndDropViewsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Binding for the place holders.
        binding.holder01.setOnDragListener(arrowDragListener)
        binding.holder02.setOnDragListener(arrowDragListener)
        binding.holder03.setOnDragListener(arrowDragListener)
        binding.holder04.setOnDragListener(arrowDragListener)
        binding.holder05.setOnDragListener(arrowDragListener)

        // Binding for the draggable arrows.
        binding.upMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.downMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.forwardMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.backMoveBtn.setOnLongClickListener(onLongClickListener)

        val rocketImage: ImageView = findViewById(R.id.rocket_image)
        rocketImage.setBackgroundResource(R.drawable.flying_rocket)

        //Creates variable that hold animation of rocket
        val rocketAnimation = rocketImage.background as AnimationDrawable

        //Button that starts the variable holding the animation
        this.binding.playButton.setOnClickListener {
            rocketAnimation.start()
        }
    }

    // When holding finger on screen drag item and create a shadow of it
    private val onLongClickListener = View.OnLongClickListener { view: View ->
        (view as? Button)?.let {

            val item = ClipData.Item(it.tag as? CharSequence)

            val dragData = ClipData(
                it.tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item
            )
            val myShadow = ArrowDragShadowBuilder(it)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(dragData, myShadow, null, 0)
            } else {
                it.startDrag(dragData, myShadow, null, 0)
            }

            true
        }
        false
    }

    // When dragging item do the following
    private val arrowDragListener = View.OnDragListener { view, dragEvent ->
        (view as? ImageView)?.let {

            // Determines actions that happen to a dragged item
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    return@OnDragListener true
                }
                // When item is no longer being dragged place item in square
                DragEvent.ACTION_DRAG_EXITED -> {
                    view.setBackgroundResource(R.drawable.squareholder1)
                    return@OnDragListener true
                }
                // Highlight Square when dragged to location
                DragEvent.ACTION_DRAG_LOCATION -> {
                    view.setBackgroundResource(R.drawable.squareholder2)
                    return@OnDragListener true
                }

                DragEvent.ACTION_DROP -> {
                //When dragged item is, determines what square displays
                    view.setBackgroundResource(R.drawable.squareholder1)

                    val item: ClipData.Item = dragEvent.clipData.getItemAt(0)
                    when (item.text.toString()) {
                    //List of images that can be shown in square holder
                        "UP" -> view.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
                        "DOWN" -> view.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
                        "FORWARD" -> view.setImageResource(R.drawable.ic_baseline_arrow_forward_24)
                        "BACK" -> view.setImageResource(R.drawable.ic_baseline_arrow_back_24)
                    }
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    return@OnDragListener true
                }
                else -> return@OnDragListener false
            }
        }
        false
    }

    //Class creates shadow for dragged items
    private class ArrowDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
        private val shadow = view.background

        //Function creates dimensions of shadow
        override fun onProvideShadowMetrics(size: Point, touch: Point) {

            val width: Int = view.width
            val height: Int = view.height

            shadow?.setBounds(0, 0, width, height)
            size.set(width, height)
            touch.set(width / 2, height / 2)
        }

        //Function that draws the shadow on canvas
        override fun onDrawShadow(canvas: Canvas) {
            shadow?.draw(canvas)
        }
    }
}