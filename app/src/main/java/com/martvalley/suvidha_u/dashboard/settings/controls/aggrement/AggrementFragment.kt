package com.martvalley.suvidha_u.dashboard.settings.controls.aggrement

import android.Manifest
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.martvalley.suvidha_u.MainApplication
import com.martvalley.suvidha_u.dashboard.settings.controls.ControlsActivity
import com.martvalley.suvidha_u.databinding.FragmentAggrementBinding
import com.martvalley.suvidha_u.utils.Constants
import com.martvalley.suvidha_u.utils.loadImage
import com.rajat.pdfviewer.PdfViewerActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class AggrementFragment : Fragment() {
    val binding by lazy { FragmentAggrementBinding.inflate(layoutInflater) }

    private val requiredPermissionList = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    private var download_file_url =
//        "https://github.com/afreakyelf/afreakyelf/raw/main/Log4_Shell_Mid_Term_final.pdf"
        "https://drive.google.com/file/d/1oR4zJzvA-tWHE2g5m0TpTBRQUrxxXeXP/view?usp=sharing"
    var per = 0f
    private val PERMISSION_CODE = 4040


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        loadAgreement()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.creditBtn.setOnClickListener {
            //launchPdf()
            binding.aggrementLinear.saveAsPdf("agreement")
        }

    }
    private fun View.toBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    private fun notifyUser(filePath: String) {
        val notificationManager = requireContext().getSystemService( NotificationManager::class.java)

        val notificationBuilder = NotificationCompat.Builder(requireContext(), "pdf_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("PDF Saved")
            .setContentText("Your PDF file has been saved at: $filePath")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager!!.notify(1, notificationBuilder.build())
    }

    private fun View.saveAsPdf(fileName: String) {
        // Create a PdfDocument
        val pdfDocument = PdfDocument()

        // Create a page description
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        // Draw the view on the page's canvas
        val canvas = page.canvas
        val bitmap = this.toBitmap()
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        // Finish the page
        pdfDocument.finishPage(page)

        // Save the PDF document
        val pdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "$fileName.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(pdfFile))
            notifyUser(pdfFile.absolutePath) // Notify user about the saved PDF
            downloadPdf(pdfFile)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }
    }

    private fun downloadPdf(file: File) {
        val fileUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant permission to read
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) // Optional: to prevent history tracking
        }

        startActivity(Intent.createChooser(intent, "Open PDF"))
    }

    private fun String.getMonth(): String {
        return when (this) {
            "01" -> "January"
            "02" -> "February"
            "03" -> "March"
            "04" -> "April"
            "05" -> "May"
            "06" -> "June"
            "07" -> "July"
            "08" -> "August"
            "09" -> "September"
            "10" -> "October"
            "11" -> "November"
            "12" -> "December"
            else -> ""
        }
    }

    private fun String.toboldText(): SpannableString {
        val boldSpannableString = SpannableString(this)
        boldSpannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            this.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return boldSpannableString
    }

    fun makeBold(input: String?): String {
        return "<b>$input</b>"
    }


    private fun launchPdf() {
        startActivity(
            PdfViewerActivity.launchPdfFromPath(
                context = requireContext(), path = "user_aggremet.pdf",
                pdfTitle = "User aggrement", directoryName = "assets", enableDownload = false,
                fromAssets = true
            )
        )
    }


    private fun loadAgreement() {
        val data = (requireActivity() as ControlsActivity).cust_data

        var agg =
            "THIS HIRE PURCHASE AGREEMENT is made and entered into at India this ${
                makeBold(
                    data?.customer?.created_at?.substring(
                        8,
                        10
                    )
                ).let { Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY) }
            }th day of ${
                data?.customer?.created_at?.substring(5, 7)?.getMonth()
            }, ${data?.customer?.created_at?.substring(0, 4)} by and between \n" +
                    "${data?.customer?.name} Store at  (hereinaftercalled referred to as the \"Owner\") of the ONE PART; ${MainApplication.authData?.name} resident of (hereinaftercalled referred to as the \"Hirer\") of the OTHER PART. WHEREAS the owner is the owner of a Mobile and the hirer has approached the owner to let the saidmobile Mobile on hire to him, to which the owner has agreed and has assured the hirer that he shall have and enjoy quiet possession of the said Mobile. \n" +
                    "AND WHEREAS the hirer has inspected the said Mobile and is satisfied about the same and considersit fit for the purposes for which he requires the same.\n NOW IT IS AGREED BETWEEN THE PARTIES AS FOLLOWS: \n" +
                    "1. The owner will let and the hirer will take on hire the Mobile. \n" +
                    "2. The owner has delivered the said Mobile to the hirer and has Mobile allowed the hirer to use the saidMobile in the usual manner. \n" +
                    "AND \n" +
                    "3. In consideration of the delivery of the said Mobile to the hirer, the latter has paid in advance a sumof Rs. ${data?.customer?.emi_amount} as first instalment on hire (the receipt whereof the owner hereby acknowledges)and will punctually pay to the owner at his place the sum of Rs. ${data?.customer?.emi_amount} every calendar month byway of rent for the hire of the said Mobile, the first payment to be made on the ${
                        data?.customer?.first_intallment_date?.substring(
                            8,
                            10
                        )
                    }th day of month and each subsequent payment on the ${
                        data?.customer?.first_intallment_date?.substring(
                            8,
                            10
                        )
                    }th day of every succeeding month. \n 4. During the continuance of hiring, the hirer shall \n" +
                    "i. Not sell, assign, pledge, mortgage, underlet, lend or part with the possession of the said Mobileand not allow the said Mobile to be used by anybody else or kept or detained or run for the use ofany other person. \n ii. Keep the said Mobile insured and kept insured so long as the hiring shall continue in the joint names of the owner and the hirer against all risks and the hirer shall pay all the premiums payable\n" +
                    "to the insurance company. On the hirer becoming the owner of the said Mobile under the terms of this agreement, the owner will transfer to the hirer the benefit of any insurance policy then currentrelating to the said Mobile.\n iii. Make good to the owner all damages to the said Mobile (fair wear and tear excepted) and pay theowner the full value of the said Mobile in the event of its totalloss. \n iv. Not use or permit or suffer the said Mobile to be used in contravention of any law for the time beingin force. \n 5. If the hirer shall duly observe and performs all the conditions herein contained and on his part tobe observed and performed and shall pay to the owner the sum specified in clause 3 hereof, together with all other sums if any payable by him to the owner under the provisions of thisAgreement, then the hiring shall come to an end and the said Mobile shall become the property ofthe hirer and the owner will assign and make over all his rights and interest in the same to the hirer, but the hirer shall have the option of purchasing the said Mobile at any time during theperiod of hiring by paying in one lump sum the balance of all the hire hereinbefore mentioned andother expenses incurred by the owner. Until all such payments as aforesaid have been made, thesaid Mobile shall remain the property of the owner.\n 6. If the hirer shall make default in payment of any monthly sum payable hereunder for ___ daysafter the same have become due or shall fail to observe or perform any of the terms and conditionsof this agreement, the owner may without prejudice to his claim for arrears of hire or damages (if any) for breach of this agreement block the Mobile completely by virtue of the software inductedin the Mobile or forthwith terminate the hiring and retake physical possession of the said Mobile himself or through his agents or servants and the hirer shall not object to the retaking of possession of the said Mobile by the owner or his agents or servants and/or by written notice to the hirer determine this agreement and the hiring hereby constituted. On such termination, the hirer shall immediately return the said Mobile to the owner at his place of address for the time being and the hirer shall pay the owner a sum of Rs ${data?.customer?.emi_amount} every month until the said Mobile is returned to the owner . \n 7. No neglect, delay or indulgence on the part of the owner in enforcing any terms or conditions ofthis agreement shall prejudice the rights of the owner hereunder.\n 8. The agreement is personal to the hirer and the rights of the hirer shall not be assignable or chargeableby him in favour of third party. \n 9. In the event of the hiring being determined by the hirer or by the owner under clause 5 hereof, thehirer shall forthwith return the said Mobile to the owner at the hirer's expense. The determinationof the hiring as aforesaid shall not affect or prejudice any claim the owner may have against thehirer for arrears of hire payments or for damages for breach of this agreement or his right toenforce such claim by action or otherwise. \n 10. The owner shall have access to the following features of the Mobile till the entire amount is notowner. \n" +
                    "i. The owner will be technical device owner and device admin of the Mobile. \n ii. The owner can completely Lock the Device. \n" +
                    "iii. The owner can disable Incoming Calls, Outgoing Calls, App Installation, File Transfer, Debuggingand Reset Mobile. \n iv. The owner can hide apps like WhatsApp, Instagram, Facebook and YouTube. \n v. The owner will have access to Mobile Number been used in the Mobile.\n vi. The owner can request for Location of the Device. \n" +
                    "vii. The owner will have access of Call Logs of the user. \n" +
                    "viii. The owner can auto Play a Audio file pre downloaded in the Mobile. \n ix. The FRP account in the device will belong to the owner of the Mobile. \n x. The owner will have access to users call logs permission, SMS permission, Location permission,Contacts permission, Mobile permission of the Mobile. \n xi. The owner will be able to perform all the above controls either through push notification orthrough Sms.\n  11. The agreement shall determine if the hirer commits any act of bankruptcy or makes any arrangementwith his creditors or on presentation of a petition in the court for adjudicating the hirer as aninsolvent or on the appointment of a receiver of the properties of the hirer or if an applicationis madeby any creditor or other person against the hirer for the attachment of the said Mobile. \n" +
                    "paid: \n" +
                    "12. The Hirer shall have the option to purchase the said Mobile, and the option shall be exercised bygiving one month's prior notice to the Owner. The option to purchase can be exercised from the dateof expiration of the stipulated period of this agreement or from any earlier date. In the former casethe Hirer shall be liable to pay to the Owner a sum equal to the Hire purchase price of the machineryand equipment mentioned in Clause (3) above, less the aggregate amount of installments paid up tothat date or Rupee one whichever is higher. In the latter case that is if the option to purchase isexercised before the expiration of the period of this agreement, the Hirer shall be liable to pay a sumequal to the said Hire-Purchase price or the balance thereof payable by monthly installments of hirecharges up to the date of the stipulated period of the agreement as reduced by a rebate which will beequal to two third of an amount which bears to the hire purchase charges the same proportion as thebalance of the hire purchase price not due till then bears to the hire purchase price.\n 13. Any dispute or claim of any nature relating in any way to your use of any Services covered underthis Agreement will be adjudicated through arbitration, by a sole arbitrator to be appointed mutuallyby the Developer and the Creditor within the 30 days of the dispute. The seat shall be decidedmutually by the parties. \n 14. The parties hereby admit that this agreement has been fully explained to them and they have\n" +
                    "understood the meaning of all the clauses of this agreement and they have signed this agreement withfull understanding of the obligations herein. \n 15. If any dispute arises between the parties out of or in connection with the agreement whether in thenature of interpretation or meaning of any term hereof or as to any claim by one against the other, orotherwise the same shall be referred to arbitration of a common arbitrator if agreed upon. otherwiseto two arbitrators one to be appointed by each party hereto and the arbitration shall be governed bythe Arbitration Act, 1940. IN WITNESS WHEREOF, the parties have hereunto set and subscribed their hands on the date and year above mentioned."
//                    "Signed and delivered by the OwnerSigned and delivered by the Hirer\n"


        binding.pdfView.text = agg
        binding.title.text = "HIRE PURCHASE AGREEMENT"

        binding.retailerSign.loadImage("${Constants.BASEURL}storage/" + MainApplication.authData?.sign)
        binding.ownerSign.loadImage("${Constants.BASEURL}storage/" + data?.customer?.sign)
    }
}