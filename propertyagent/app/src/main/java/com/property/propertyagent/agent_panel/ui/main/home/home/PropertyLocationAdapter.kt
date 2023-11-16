package com.property.propertyagent.agent_panel.ui.main.home.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.details.OwnerPropertyViewDetailedActivity
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyappointment.PropertyAppointmentActivity
import com.property.propertyagent.modal.agent.agent_home.Appoinment
import com.property.propertyagent.start_up.google_map.PropertyMapsActivity
import com.property.propertyagent.utils.AppPreferences.is_user
import com.property.propertyagent.utils.AppPreferences.tour_id
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.CommonUtils.Companion.showUserProfile
import kotlinx.android.synthetic.main.recycle_product_list_item.view.*

class PropertyLocationAdapter(
    private var activity: Activity,
    private var appointmentBookings: List<Appoinment>,
) : RecyclerView.Adapter<PropertyLocationAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_product_list_item, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int {
        return appointmentBookings.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.itemView.PropertLocationAdapter_start_time.text = appointmentBookings[position].time
        if (appointmentBookings[position].property_details.latitude != "") {
            try {
                holder.itemView.PropertLocationAdapter_location.text = CommonUtils.getShortAddress(
                    appointmentBookings[position].property_details.latitude.toDouble(),
                    appointmentBookings[position].property_details.longitude.toDouble(), context!!
                )
            } catch (e: Exception) {
                e.printStackTrace()
                holder.itemView.PropertLocationAdapter_location.text =
                    context?.getString(R.string.location)
            }
        } else {
            holder.itemView.PropertLocationAdapter_location.text =
                context?.getString(R.string.location)
        }

        if (appointmentBookings[position].type == 2) {
            holder.itemView.PropertLocationAdapter_type.text = context?.getString(R.string.owner)
        } else {
            holder.itemView.PropertLocationAdapter_type.text = context?.getString(R.string.user)
        }

        holder.itemView.PropertLocationAdapter_startbtn.setOnClickListener {
            if (appointmentBookings[position].type == 2) {
                val intent = Intent(context, OwnerPropertyViewDetailedActivity::class.java)
                intent.putExtra("tour_id", appointmentBookings[position].tour_id.toString())
                intent.putExtra("type", "tour")
                context?.startActivity(intent)
            } else {
                val intent = Intent(context, PropertyAppointmentActivity::class.java)
                intent.putExtra("tour_id", appointmentBookings[position].tour_id.toString())
                context?.startActivity(intent)
            }
        }

        holder.itemView.PropertLocationAdapter_location.paintFlags =
            holder.itemView.PropertLocationAdapter_location.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        holder.itemView.PropertLocationAdapter_location.setOnClickListener {
            is_user = appointmentBookings[position].type != 2
            tour_id = appointmentBookings[position].tour_id.toString()
            context?.startActivity(Intent(context, PropertyMapsActivity::class.java))
        }

        holder.itemView.PropertLocationAdapter_type.paintFlags =
            holder.itemView.PropertLocationAdapter_type.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        holder.itemView.PropertLocationAdapter_type.setOnClickListener {
            if (appointmentBookings[position].type == 2) {
                showUserProfile(
                    activity,
                    context!!.getString(R.string.owner),
                    appointmentBookings[position].owner_details.name,
                    appointmentBookings[position].owner_details.phone,
                    appointmentBookings[position].owner_details.profile_image
                )
            } else {
                showUserProfile(
                    activity,
                    context!!.getString(R.string.user),
                    appointmentBookings[position].user_details.name,
                    appointmentBookings[position].user_details.phone,
                    appointmentBookings[position].user_details.profile_pic
                )
            }

/*

            val bottom = BottomSheetDialog(activity, R.style.ThemeOverlay_App_BottomSheetDialog)
            val bottomSheet: View =
                activity.layoutInflater.inflate(R.layout.profile_layout_bottom_sheet, null)
            val closeBtn = bottomSheet.findViewById<ImageButton>(R.id.ivClose)
            val imageView =
                bottomSheet.findViewById<CircleImageView>(R.id.civProfilePicRequestDialog)
            val name = bottomSheet.findViewById<TextView>(R.id.tvProfileNameRequestDialog)
            val userType = bottomSheet.findViewById<TextView>(R.id.tvUserTypeRequestDialog)
            val callBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivPhoneRequestDialog)
            val msgBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivWhatsappRequestDialog)

            closeBtn.setOnClickListener {
                bottom.dismiss()
            }

            if (appointmentBookings[position].type == 2) {
                if (appointmentBookings[position].owner_details.profile_image.isNotBlank()) {
                    imageView.loadImagesWithGlideExt(appointmentBookings[position].owner_details.profile_image)
                }
                name.text = appointmentBookings[position].owner_details.name
                userType.text = context!!.getString(R.string.owner)
                if (appointmentBookings[position].owner_details.phone.isNotBlank()) {
                    callBtn.setOnClickListener {
                        val ph: String = appointmentBookings[position].owner_details.phone
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:$ph")
                        context?.startActivity(intent)
                    }
                    msgBtn.setOnClickListener {
                        openWhatsAppEnquiry(
                            context!!,
                            appointmentBookings[position].owner_details.phone,
                            context!!.getString(R.string.hai)
                        )
                    }
                }
            } else {
                if (appointmentBookings[position].user_details.profile_pic.isNotBlank()) {
                    imageView.loadImagesWithGlideExt(appointmentBookings[position].user_details.profile_pic)
                }
                name.text = appointmentBookings[position].user_details.name
                userType.text = context!!.getString(R.string.user)
                if (appointmentBookings[position].user_details.phone.isNotBlank()) {
                    callBtn.setOnClickListener {
                        val ph: String = appointmentBookings[position].user_details.phone
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:$ph")
                        context?.startActivity(intent)
                    }
                    msgBtn.setOnClickListener {
                        openWhatsAppEnquiry(
                            context!!,
                            appointmentBookings[position].user_details.phone,
                            context!!.getString(R.string.hai)
                        )
                    }
                }
            }


            bottom.setContentView(bottomSheet)
            bottom.show()
*/

            /* val dialog = context?.let { it1 -> Dialog(it1) }
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

             if (appointmentBookings[position].type == 2) {
                 if (appointmentBookings[position].owner_details.profile_image.isNotBlank()) {
                     civProfilePicRequestDialog.loadImagesWithGlideExt(appointmentBookings[position].owner_details.profile_image)
                 }
                 tvProfileNameRequestDialog.text = appointmentBookings[position].owner_details.name
                 tvUserTypeRequestDialog.text = context!!.getString(R.string.owner)
                 if (appointmentBookings[position].owner_details.phone.isNotBlank()) {
                     ivPhoneRequestDialog.setOnClickListener {
                         val ph: String = appointmentBookings[position].owner_details.phone
                         val intent = Intent(Intent.ACTION_DIAL)
                         intent.data = Uri.parse("tel:$ph")
                         context?.startActivity(intent)
                     }
                     ivWhatsappRequestDialog.setOnClickListener {
                         openWhatsAppEnquiry(
                             context!!,
                             appointmentBookings[position].owner_details.phone,
                             context!!.getString(R.string.hai)
                         )
                     }
                 }
             } else {
                 if (appointmentBookings[position].user_details.profile_pic.isNotBlank()) {
                     civProfilePicRequestDialog.loadImagesWithGlideExt(appointmentBookings[position].user_details.profile_pic)
                 }
                 tvProfileNameRequestDialog.text = appointmentBookings[position].user_details.name
                 tvUserTypeRequestDialog.text = context!!.getString(R.string.user)
                 if (appointmentBookings[position].user_details.phone.isNotBlank()) {
                     ivPhoneRequestDialog.setOnClickListener {
                         val ph: String = appointmentBookings[position].user_details.phone
                         val intent = Intent(Intent.ACTION_DIAL)
                         intent.data = Uri.parse("tel:$ph")
                         context?.startActivity(intent)
                     }
                     ivWhatsappRequestDialog.setOnClickListener {
                         openWhatsAppEnquiry(
                             context!!,
                             appointmentBookings[position].user_details.phone,
                             context!!.getString(R.string.hai)
                         )
                     }
                 }
             }

             dialog.show()
             val layoutParams = WindowManager.LayoutParams()
             layoutParams.copyFrom(dialog.window?.attributes)
             layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
             layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
             dialog.window?.attributes = layoutParams
 */
        }
    }
}