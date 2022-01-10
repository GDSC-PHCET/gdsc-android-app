package com.finite.gdscphcet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.finite.gdscphcet.databinding.ActivityCertificateBinding
import com.finite.gdscphcet.ui.CertificateViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CertificateActivity : AppCompatActivity() {

    private val viewModel : CertificateViewModel by viewModels()
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCertificateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding?.apply {
            certificateVm = viewModel
            certificateActivity = this@CertificateActivity
        }

        binding.verifyButton.setOnClickListener{
            var text: String = binding.codeEditText.text.toString().trim()
            if (text.isEmpty()) {
                binding.codeTextField.setError("Error! Enter the Code!")
            }
            else {
                text = text.uppercase()

                database = FirebaseDatabase.getInstance().getReference("certificates")
                database.child(text).get().addOnSuccessListener {

                    if (it.exists()) { // Real Certificate
                        binding.codeTextField.setError(null)
                        binding.certstatus.text = "Status : Verified Certificate"
                        binding.certissuedto.text = "Issued To :  " + it.child("issuedTo").value.toString()
                        binding.certissuedby.text = "Signed By :  " + it.child("issuedBy").value.toString()
                        binding.certissuedate.text ="Issued Date :  " +  it.child("issueDate").value.toString()
                        binding.certeventname.text ="Event Name :  " +  it.child("eventName").value.toString()
                        binding.certtype.text = "Certificate Type :  " + it.child("type").value.toString()

                        Toast.makeText(this, "Verification Successful!",Toast.LENGTH_SHORT).show()


                    } else {
                        // Fake Certificate
                        binding.codeTextField.setError("Invalid Code")

                        Toast.makeText(this, "InvalidCode : $text",Toast.LENGTH_SHORT).show()

                        binding.certstatus.text = "Status : Invalid Certificate Code"
                        binding.certissuedto.text = ""
                        binding.certissuedby.text = ""
                        binding.certissuedate.text = ""
                        binding.certeventname.text = ""
                        binding.certtype.text = ""
                    }

                }.addOnFailureListener {

                    Toast.makeText(this, "OnFailureCalled",Toast.LENGTH_SHORT).show()

                }

            }

        }

    }
}