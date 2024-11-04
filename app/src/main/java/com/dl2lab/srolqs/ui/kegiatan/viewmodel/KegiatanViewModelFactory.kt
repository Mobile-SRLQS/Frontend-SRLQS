import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.data.repository.KegiatanRepository
import com.dl2lab.srolqs.ui.kegiatan.viewmodel.KegiatanViewModel

class KegiatanViewModelFactory(private val kegiatanRepository: KegiatanRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KegiatanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KegiatanViewModel(kegiatanRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}