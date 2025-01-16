package com.dl2lab.srolqs.ui.kegiatan.detail

import KegiatanViewModelFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dl2lab.srolqs.databinding.FragmentDetailKegiatanBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.kegiatan.viewmodel.KegiatanViewModel
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.akndmr.library.AirySnackbar
import com.akndmr.library.AirySnackbarSource
import com.akndmr.library.AnimationAttribute
import com.akndmr.library.GravityAttribute
import com.akndmr.library.RadiusAttribute
import com.akndmr.library.SizeAttribute
import com.akndmr.library.SizeUnit
import com.akndmr.library.TextAttribute
import com.akndmr.library.Type
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.response.KegiatanItem
import com.dl2lab.srolqs.data.repository.KegiatanRepository
import com.dl2lab.srolqs.ui.customview.LoadingManager
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailKegiatanFragment : Fragment() {
    private var kegiatanId: Int? = null
    private var _binding: FragmentDetailKegiatanBinding? = null
    private val binding get() = _binding!!
    var isTokenLoaded = false
    var isDataLoaded = false

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val viewModel: KegiatanViewModel by viewModels {
        KegiatanViewModelFactory(KegiatanRepository())
    }
    private var kegiatanItem: KegiatanItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailKegiatanBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            kegiatanId = it.getInt("kegiatanId")
        }
        LoadingManager.init(this)
        getKegiatanDetail()
        if (kegiatanId != null) {
            fetchDetail()
        } else {

        }
    }

    private fun fetchDetail() {
        arguments?.let {
            kegiatanId = it.getInt("kegiatanId")
        }
        mainViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            user.token?.let { token ->
                isTokenLoaded = true
                viewModel.fetchDetailKegiatan(kegiatanId!!, token)
            }
        })
    }

    private fun getKegiatanDetail() {


        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                LoadingManager.show()
                binding.llDetailKegiatan.visibility = View.GONE
            } else if (isTokenLoaded && !isLoading) {
                LoadingManager.hide()
                binding.llDetailKegiatan.visibility = View.VISIBLE

            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                requireContext().showCustomAlertDialog("Gagal memuat daftar kegiatan",
                    errorMessage ?: "Terjadi kesalahan saat memuat daftar kegiatan",
                    "OK",
                    "",
                    {},
                    {})
            }

        })


        viewModel.checklistResult.observe(viewLifecycleOwner) { result ->
            LoadingManager.hide()
            if (result != null) {

                AirySnackbar.make(
                    source = AirySnackbarSource.ViewSource(binding.root),
                    type = Type.Success,
                    attributes = listOf(
                        TextAttribute.Text(text = "Berhasil menyelesaikan kegiatan!"),
                        TextAttribute.TextColor(textColor = R.color.white),
                        SizeAttribute.Margin(left = 24, right = 24, unit = SizeUnit.DP),
                        SizeAttribute.Padding(top = 12, bottom = 12, unit = SizeUnit.DP),
                        RadiusAttribute.Radius(radius = 8f),
                        GravityAttribute.Bottom,
                        AnimationAttribute.FadeInOut
                    )
                ).show()
                fetchDetail()


            } else {
                result?.message?.let {
                    requireContext().showCustomAlertDialog("Gagal Menyelesaikan Kegiatan",
                        it,
                        "OK",
                        "",
                        {

                        },
                        {})
                }
            }
        }

        viewModel.deleteResult.observe(viewLifecycleOwner) { result ->
            LoadingManager.hide()
            if (result != null) {

                AirySnackbar.make(
                    source = AirySnackbarSource.ViewSource(binding.root),
                    type = Type.Success,
                    attributes = listOf(
                        TextAttribute.Text(text = "Berhasil menghapus kegiatan!"),
                        TextAttribute.TextColor(textColor = R.color.white),
                        SizeAttribute.Margin(left = 24, right = 24, unit = SizeUnit.DP),
                        SizeAttribute.Padding(top = 12, bottom = 12, unit = SizeUnit.DP),
                        RadiusAttribute.Radius(radius = 8f),
                        GravityAttribute.Bottom,
                        AnimationAttribute.FadeInOut
                    )
                ).show()
                findNavController().popBackStack()


            } else {
                result?.message?.let {
                    requireContext().showCustomAlertDialog("Gagal menghapus kegiatan",
                        it,
                        "OK",
                        "",
                        {

                        },
                        {})
                }
            }
        }

        viewModel.kegiatanItem.observe(viewLifecycleOwner, Observer { item ->
            isDataLoaded = true
            if (isTokenLoaded && isDataLoaded) {
                LoadingManager.hide()
            }

            if (item != null) {
                kegiatanItem = item
                binding.tvNamaKegiatan.text = item.namaKegiatan
                binding.tvTipeKegiatan.text = item.tipeKegiatan
                binding.tvTanggal.text = formatTanggal(item.tenggat)
                binding.tvLink.text = item.link
                binding.tvCatatan.text = item.catatan
                binding.btnEdit.setOnClickListener {
                    val action =
                        DetailKegiatanFragmentDirections.actionNavigationDetailKegiatanToEditKegiatanFragment(
                            item.id
                        )
                    findNavController().navigate(action)
                }
                binding.btnDelete.setOnClickListener {
                    requireContext().showCustomAlertDialog(
                        "Peringatan",
                        "Apakah Anda yakin ingin menghapus kegiatan \' ${item.namaKegiatan}\'?",
                        "Ya",
                        "Batalkan",
                        {
                            mainViewModel.getSession()
                                .observe(viewLifecycleOwner, Observer { user ->
                                    user.token?.let { token ->
                                        isTokenLoaded = true
                                        viewModel.deleteKegiatan(item.id, token)
                                    }
                                })
                        },
                        {},
                        showIcon = false
                    )

                }
                if (item.isDone) {
                    binding.btnMarkAsDone.visibility = View.GONE
                } else {
                    binding.btnMarkAsDone.setOnClickListener {
                        mainViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
                            user.token?.let { token ->
                                isTokenLoaded = true
                                viewModel.checklistKegiatan(item.id, token)
                            }
                        })
                    }
                }

            } else {
                requireContext().showCustomAlertDialog("Gagal memuat detail kegiatan",
                    "Terjadi kesalahan saat memuat detail kegiatan",
                    "OK",
                    "",
                    {},
                    {})
            }
        })
    }

    fun formatTanggal(tanggal: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        return try {
            val date: Date = inputFormat.parse(tanggal) ?: return tanggal
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            tanggal
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}