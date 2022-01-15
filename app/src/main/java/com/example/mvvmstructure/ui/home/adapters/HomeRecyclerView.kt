package com.example.mvvmstructure.ui.home.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.mvvmstructure.databinding.ItemViewProductBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Custom RecyclerView to support playing videos, Actually There are two support
 * items: image - video
 * When user scroll items, Actually in onScrollStateChanged method detect the
 * item position to play video(if item is video content).
 */
@AndroidEntryPoint
class HomeRecyclerView : RecyclerView {

    // Inject instance of video player, It is singleTone
    @Inject
    lateinit var videoPlayer: SimpleExoPlayer

    @Inject
    lateinit var glide: RequestManager

    /**
     * Video player view
     * Actually need to add this view when new video content item
     * detected and remove it when scroll to new item.
     * add or remove to [R.layout.frame_media_container] in [R.layout.item_view_product]
     */
    private lateinit var videoSurfaceView: PlayerView
    private var productBinding: ItemViewProductBinding? = null
    private var viewHolderParent: View? = null

    private var videoSurfaceDefaultHeight = 0
    private var screenDefaultHeight = 0

    private var targetPosition = -1
    private var playPosition = -1
    private var isVideoViewAdded = false

    private var timeHashMap = HashMap<Int, Long>()
    private var videoPosition = -1

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        val display =
            (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val point = Point()
        display.getSize(point)
        videoSurfaceDefaultHeight = point.x
        screenDefaultHeight = point.y

        // setup PlayerView
        videoSurfaceView = PlayerView(context)
        videoSurfaceView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        videoSurfaceView.useController = false
        videoSurfaceView.setShutterBackgroundColor(Color.TRANSPARENT)

        videoPlayer.volume = 1f

        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(1)) {
                        val targetPosition = getTargetPosition(true)
                        this@HomeRecyclerView.targetPosition = targetPosition
                        onScrollChange(targetPosition)
                    } else {
                        val targetPosition = getTargetPosition(false)
                        this@HomeRecyclerView.targetPosition = targetPosition
                        onScrollChange(targetPosition)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}

            override fun onChildViewDetachedFromWindow(view: View) {
                if (viewHolderParent != null && viewHolderParent == view) {
                    resetVideoView()
                    playPosition = -1
                }
            }

        })

        videoPlayer.addListener(object : Player.EventListener {
            override fun onTimelineChanged(
                timeline: Timeline,
                @Nullable manifest: Any?,
                reason: Int
            ) {
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
            }

            override fun onLoadingChanged(isLoading: Boolean) {}
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        if (productBinding != null) {
                            productBinding?.progressMedia?.visibility = VISIBLE
                        }
                    }
                    Player.STATE_ENDED -> {
                        videoPlayer.seekTo(0)
                    }
                    Player.STATE_IDLE -> {
                    }
                    Player.STATE_READY -> {
                        if (productBinding != null) {
                            productBinding?.progressMedia?.visibility = GONE
                        }
                        if (!isVideoViewAdded) {
                            addVideoView()
                        }
                    }
                    else -> {

                    }
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {}
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
            override fun onPlayerError(error: ExoPlaybackException) {}
            override fun onPositionDiscontinuity(reason: Int) {}
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            override fun onSeekProcessed() {}
        })
    }

    private fun onScrollChange(targetPosition: Int) {
        if (!isTheSamePosition(targetPosition)) {

            if (productBinding != null && isVideoViewAdded) {
                resetVideoView()
            }

            productBinding?.progressMedia?.visibility = GONE

            if ((adapter as ProductAdapter).getProducts()[targetPosition].type == "video") {
                if (getDataFromAdapter(targetPosition)) {
                    if (productBinding != null) {
                        productBinding?.progressMedia?.visibility = VISIBLE
                    }

                    videoSurfaceView.player = videoPlayer
                    videoPosition = playPosition

                    val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                        context, Util.getUserAgent(context, "PostRecyclerView")
                    )

                    val mediaUrl: String? =
                        (adapter as ProductAdapter).getProducts()[targetPosition].videoUrl
                    val videoSource: MediaSource =
                        ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(mediaUrl))
                    videoPlayer.prepare(videoSource)
                    videoPlayer.playWhenReady = true

                    if (timeHashMap.containsKey(videoPosition)) {
                        videoPlayer.seekTo(timeHashMap[videoPosition]!!)
                    } else {
                        videoPlayer.seekTo(0)
                    }
                }
            } else {
                getDataFromAdapter(targetPosition)
            }
        }
    }

    private fun isTheSamePosition(targetPosition: Int): Boolean {
        if (targetPosition == playPosition) {
            return true
        }
        playPosition = targetPosition
        return false
    }

    private fun getDataFromAdapter(targetPosition: Int): Boolean {
        val currentPosition =
            targetPosition - (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        val child = getChildAt(currentPosition) ?: return false
        val holder = child.tag as ProductAdapter.ProductViewHolder
        productBinding = holder.binding
        viewHolderParent = holder.itemView
//        viewHolderParent?.setOnClickListener(videoViewClickListener)
        return true
    }

    private fun getTargetPosition(isEndOfList: Boolean): Int {
        val targetPosition: Int
        if (!isEndOfList) {
            var startPosition =
                (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
            var endPosition =
                (layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()

            // if there is more than 2 list-items on the screen, set the difference to be 1

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1
            }

            // something is wrong. return.

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return -1
            }

            // if there is more than 1 list-item on the screen
            targetPosition = if (startPosition != endPosition) {
                val startPositionVideoHeight: Int = getVisibleVideoSurfaceHeight(startPosition)
                val endPositionVideoHeight: Int = getVisibleVideoSurfaceHeight(endPosition)
                if (startPositionVideoHeight > endPositionVideoHeight) startPosition else endPosition
            } else {
                startPosition
            }
        } else {
            targetPosition = (adapter as ProductAdapter).itemCount - 1
        }
        return targetPosition
    }

    private fun getVisibleVideoSurfaceHeight(playPosition: Int): Int {
        val at =
            playPosition - (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        val child = getChildAt(at) ?: return 0
        val location = IntArray(2)
        child.getLocationInWindow(location)
        return if (location[1] < 0) {
            location[1] + videoSurfaceDefaultHeight
        } else {
            screenDefaultHeight - location[1]
        }
    }

    // Remove the old player
    private fun removeVideoView(videoView: PlayerView) {
        val parent = videoView.parent as ViewGroup? ?: return
        val index = parent.indexOfChild(videoView)
        if (index >= 0) {
            parent.removeViewAt(index)
            isVideoViewAdded = false
            viewHolderParent?.setOnClickListener(null)
            videoSurfaceView.player = null
        }
    }

    private fun addVideoView() {
        productBinding?.frameMediaContainer?.addView(videoSurfaceView)
        isVideoViewAdded = true
        videoSurfaceView.requestFocus()
        videoSurfaceView.visibility = VISIBLE
        videoSurfaceView.alpha = 1f
        productBinding?.imageThumbnail?.visibility = GONE
    }

    private fun resetVideoView() {
        if (isVideoViewAdded) {
            removeVideoView(videoSurfaceView)
            videoSurfaceView.visibility = INVISIBLE
            productBinding?.imageThumbnail?.visibility = VISIBLE
            if (timeHashMap.containsKey(videoPosition)) {
                timeHashMap.remove(videoPosition)
            }
            timeHashMap[videoPosition] = videoPlayer.currentPosition!!
            videoPlayer.stop()
        }
    }
}