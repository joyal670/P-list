package com.property.propertyagent.agent_panel.ui.main.home.request

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.modal.agent.agent_my_request_list.Requested
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.recycle_myrequest_list_item.view.*

class RequestApartmentAdapter(
    private var activity: Activity,
    private var requestList: List<Requested>,
    private val phoneClick: (String) -> Unit,
    private val acceptRequestClick: (String, String, Int) -> Unit,
    private val rejectRequestClick: (String, String, Int) -> Unit,
) : RecyclerView.Adapter<RequestApartmentAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_myrequest_list_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        // set underlines
        holder.itemView.myrequest_item_locationName.paintFlags =
            holder.itemView.myrequest_item_locationName.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        holder.itemView.myrequest_item_viewProfile.paintFlags =
            holder.itemView.myrequest_item_viewProfile.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        holder.itemView.tvDateRequest.text = requestList[position].date
        holder.itemView.tvTimeRequest.text = requestList[position].time
        if (!requestList[position].property_priority_image.equals(null)) {
            holder.itemView.ivPropertyImageRequestNew.loadImagesWithGlideExt(requestList[position].property_priority_image.document)
        }
        holder.itemView.tvProfileTypeRequest.text = requestList[position].type
        if (requestList[position].type.equals(
                "Owner",
                ignoreCase = true
            )
        ) {
            if (!requestList[position].user_property_related.equals(null)) {
                if (requestList[position].user_property_related.property_to == 0) {
                    holder.itemView.tvPropertyToRequest.text =
                        context!!.getString(R.string.appartment_for_rent)
                } else {
                    holder.itemView.tvPropertyToRequest.text =
                        context!!.getString(R.string.appartment_for_sale)
                }

                try {
                    holder.itemView.myrequest_item_locationName.text = CommonUtils.getAddress(
                        requestList[position].user_property_related.latitude,
                        requestList[position].user_property_related.longitude, context!!
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    holder.itemView.myrequest_item_locationName.text =
                        context?.getString(R.string.location)
                }

                if (!requestList[position].owner_rel.equals(null)) {
                    holder.itemView.ivProfileImageRequest.loadImagesWithGlideExt(
                        requestList[position].owner_rel.profile_image
                    )
                    holder.itemView.tvProfileNameRequest.text = requestList[position].owner_rel.name
                }
            }
            holder.itemView.myrequest_item_viewProfile.setOnClickListener {
                CommonUtils.showUserProfile(activity,requestList[position].type, requestList[position].owner_rel.name, requestList[position].owner_rel.phone, requestList[position].owner_rel.profile_image)

          /*      val bottom = BottomSheetDialog(context!!, R.style.ThemeOverlay_App_BottomSheetDialog)
                val bottomSheet: View = activity.layoutInflater.inflate(R.layout.profile_layout_bottom_sheet, null)
                val closeBtn = bottomSheet.findViewById<ImageButton>(R.id.ivClose)
                val imageView = bottomSheet.findViewById<CircleImageView>(R.id.civProfilePicRequestDialog)
                val name = bottomSheet.findViewById<TextView>(R.id.tvProfileNameRequestDialog)
                val userType = bottomSheet.findViewById<TextView>(R.id.tvUserTypeRequestDialog)
                val callBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivPhoneRequestDialog)
                val msgBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivWhatsappRequestDialog)

                closeBtn.setOnClickListener {
                    bottom.dismiss()
                }
                imageView.loadImagesWithGlideExt(requestList[position].owner_rel.profile_image)
                name.text = requestList[position].owner_rel.name
                userType.text = requestList[position].type
                callBtn.setOnClickListener {
                    if (requestList[position].owner_rel.phone.isNotBlank()) {
                        phoneClick.invoke(requestList[position].owner_rel.phone)
                    }
                }
                msgBtn.setOnClickListener {
                    if (requestList[position].owner_rel.phone.isNotBlank()) {
                        val message = "Hallo"
                        context!!.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    String.format(
                                        "https://api.whatsapp.com/send?phone=%s&text=%s",
                                        requestList[position].owner_rel.phone,
                                        message
                                    )
                                )
                            )
                        )
                    }

                }



                bottom.setContentView(bottomSheet)
                bottom.show()*/


              /*  val dialog = context?.let { it1 -> Dialog(it1) }
                dialog?.setCancelable(true)
                dialog?.setContentView(R.layout.profile_layout)
                val civProfilePicRequestDialog =
                    dialog!!.findViewById(R.id.civProfilePicRequestDialog) as CircleImageView
                val tvProfileNameRequestDialog =
                    dialog.findViewById(R.id.tvProfileNameRequestDialog) as TextView
                val tvUserTypeRequestDialog =
                    dialog.findViewById(R.id.tvUserTypeRequestDialog) as TextView
                val ivPhoneRequestDialog =
                    dialog.findViewById(R.id.ivPhoneRequestDialog) as ImageView
                val ivWhatsappRequestDialog =
                    dialog.findViewById(R.id.ivWhatsappRequestDialog) as ImageView

                civProfilePicRequestDialog.loadImagesWithGlideExt(requestList[position].owner_rel.profile_image)
                tvProfileNameRequestDialog.text = requestList[position].owner_rel.name
                tvUserTypeRequestDialog.text = requestList[position].type
                if (requestList[position].owner_rel.phone.isNotBlank()) {
                    ivPhoneRequestDialog.setOnClickListener {
                        phoneClick.invoke(requestList[position].owner_rel.phone)
                    }
                }
                if (requestList[position].owner_rel.phone.isNotBlank()) {
                    ivWhatsappRequestDialog.setOnClickListener {
                        val message = "Hallo"
                        context!!.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    String.format(
                                        "https://api.whatsapp.com/send?phone=%s&text=%s",
                                        requestList[position].owner_rel.phone,
                                        message
                                    )
                                )
                            )
                        )
                    }
                }

                dialog.show()
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                dialog.window?.attributes = layoutParams*/
            }
        } else {
            if (!requestList[position].property_details.equals(null)) {
                if (requestList[position].property_details.property_to == 0) {
                    holder.itemView.tvPropertyToRequest.text =
                        context!!.getString(R.string.appartment_for_rent)
                } else {
                    holder.itemView.tvPropertyToRequest.text =
                        context!!.getString(R.string.appartment_for_sale)
                }
                if (requestList[position].property_details.latitude.isNotBlank() &&
                    requestList[position].property_details.longitude.isNotBlank()
                ) {
                    try {
                        holder.itemView.myrequest_item_locationName.text = CommonUtils.getAddress(
                            requestList[position].property_details.latitude,
                            requestList[position].property_details.longitude, context!!
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        holder.itemView.myrequest_item_locationName.text =
                            context?.getString(R.string.location)
                    }
                }
                if (!requestList[position].user_rel.equals(null)) {
                    holder.itemView.ivProfileImageRequest.loadImagesWithGlideExt(
                        requestList[position].user_rel.profile_pic
                    )
                    holder.itemView.tvProfileNameRequest.text = requestList[position].user_rel.name
                }
            }
            holder.itemView.myrequest_item_viewProfile.setOnClickListener {

                CommonUtils.showUserProfile(
                    activity,
                    requestList[position].type,
                    requestList[position].user_rel.name,
                    requestList[position].user_rel.phone,
                    requestList[position].user_rel.profile_pic
                )


                /*val bottom = BottomSheetDialog(context!!, R.style.ThemeOverlay_App_BottomSheetDialog)
                val bottomSheet: View = activity.layoutInflater.inflate(R.layout.profile_layout_bottom_sheet, null)
                val closeBtn = bottomSheet.findViewById<ImageButton>(R.id.ivClose)
                val imageView = bottomSheet.findViewById<CircleImageView>(R.id.civProfilePicRequestDialog)
                val name = bottomSheet.findViewById<TextView>(R.id.tvProfileNameRequestDialog)
                val userType = bottomSheet.findViewById<TextView>(R.id.tvUserTypeRequestDialog)
                val callBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivPhoneRequestDialog)
                val msgBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivWhatsappRequestDialog)

                closeBtn.setOnClickListener {
                    bottom.dismiss()
                }
                imageView.loadImagesWithGlideExt(requestList[position].user_rel.profile_pic)
                name.text = requestList[position].user_rel.name
                userType.text = requestList[position].type
                callBtn.setOnClickListener {
                    if (requestList[position].user_rel.phone.isNotBlank()) {
                        phoneClick.invoke(requestList[position].user_rel.phone)
                    }
                }
                msgBtn.setOnClickListener {
                    if (requestList[position].user_rel.phone.isNotBlank()) {
                        val message = "Hallo"
                        context!!.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    String.format(
                                        "https://api.whatsapp.com/send?phone=%s&text=%s",
                                        requestList[position].user_rel.phone,
                                        message
                                    )
                                )
                            )
                        )
                    }

                }



                bottom.setContentView(bottomSheet)
                bottom.show()*/

             /*   val dialog = context?.let { it1 -> Dialog(it1) }
                dialog?.setCancelable(true)
                dialog?.setContentView(R.layout.profile_layout)
                val civProfilePicRequestDialog =
                    dialog!!.findViewById(R.id.civProfilePicRequestDialog) as CircleImageView
                val tvProfileNameRequestDialog =
                    dialog.findViewById(R.id.tvProfileNameRequestDialog) as TextView
                val tvUserTypeRequestDialog =
                    dialog.findViewById(R.id.tvUserTypeRequestDialog) as TextView
                val ivPhoneRequestDialog =
                    dialog.findViewById(R.id.ivPhoneRequestDialog) as ImageView
                val ivWhatsappRequestDialog =
                    dialog.findViewById(R.id.ivWhatsappRequestDialog) as ImageView

                try {
                    civProfilePicRequestDialog.loadImagesWithGlideExt(requestList[position].user_rel.profile_pic)
                    tvProfileNameRequestDialog.text = requestList[position].user_rel.name
                    tvUserTypeRequestDialog.text = requestList[position].type
                    if (requestList[position].user_rel.phone.isNotBlank()) {
                        ivPhoneRequestDialog.setOnClickListener {
                            phoneClick.invoke(requestList[position].user_rel.phone)
                        }
                    }
                    if (requestList[position].user_rel.phone.isNotBlank()) {
                        ivWhatsappRequestDialog.setOnClickListener {
                            val message = "Hallo"
                            context!!.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(
                                        String.format(
                                            "https://api.whatsapp.com/send?phone=%s&text=%s",
                                            requestList[position].user_rel.phone,
                                            message
                                        )
                                    )
                                )
                            )
                        }
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

                dialog.show()
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                dialog.window?.attributes = layoutParams*/

            }
        }
        holder.itemView.btnDeniedRequest.setOnClickListener {
            if (requestList[position].type.equals(
                    "Owner",
                    ignoreCase = true
                )
            ) {
                rejectRequestClick.invoke(requestList[position].id.toString(), "1", position)
            } else {
                rejectRequestClick.invoke(requestList[position].id.toString(), "2", position)
            }
        }
        holder.itemView.btnAcceptRequest.setOnClickListener {
            if (requestList[position].type.equals(
                    "Owner",
                    ignoreCase = true
                )
            ) {
                acceptRequestClick.invoke(requestList[position].id.toString(), "1", position)
            } else {
                acceptRequestClick.invoke(requestList[position].id.toString(), "2", position)
            }
        }
    }
}