package com.ranuskin.ranloock.pepo


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ranuskin.ranloock.pepo.Objects.LostPet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details_for_lost_found.*


class DetailsForLostFoundFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_for_lost_found, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        bundle?.let { bundle->
            val pet = bundle.getSerializable("pet") as LostPet
            fragment_details_pet_name_textview.text = pet.name
            fragment_details_lost_date.text = pet.date
            fragment_details_lost_description.text = pet.description
            fragment_details_lost_owner_name_textview.text = pet.owner
            fragment_details_lost_owner_number.text = pet.phone
            Picasso.get().load(pet.imageURL).placeholder(R.drawable.gil_daniel).into(fragment_details_for_lost_found_pet_imageview)
        }
    }
}
