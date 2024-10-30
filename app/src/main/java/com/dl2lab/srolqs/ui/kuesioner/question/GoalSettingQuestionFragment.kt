package com.dl2lab.srolqs.ui.kuesioner.question

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.FragmentGoalSettingQuestionBinding
import com.dl2lab.srolqs.ui.authentication.register.RegisterActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GoalSettingQuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalSettingQuestionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentGoalSettingQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoalSettingQuestionBinding.inflate(layoutInflater)
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
                replace(R.id.questionnaire_container, EnvironmentStructuringQuestionFragment(), EnvironmentStructuringQuestionFragment::class.java.simpleName)
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GoalSettingQuestionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GoalSettingQuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}