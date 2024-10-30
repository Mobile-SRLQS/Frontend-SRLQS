package com.dl2lab.srolqs.ui.kuesioner.question

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.FragmentEnvironmentStructuringQuestionBinding
import com.dl2lab.srolqs.databinding.FragmentGoalSettingQuestionBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EnvirontmentStructuringQuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnvironmentStructuringQuestionFragment : Fragment() {
    private lateinit var binding: FragmentEnvironmentStructuringQuestionBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnvironmentStructuringQuestionBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupAction() {
        binding.nextButton.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.questionnaire_container, TaskStrategyQuestionFragment(), TaskStrategyQuestionFragment::class.java.simpleName)
            }
        }
        binding.prevButton.setOnClickListener {
            parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.questionnaire_container, GoalSettingQuestionFragment(), GoalSettingQuestionFragment::class.java.simpleName)
        } }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EnvirontmentStructuringQuestionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EnvironmentStructuringQuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}