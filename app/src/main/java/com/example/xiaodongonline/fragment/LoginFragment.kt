package com.example.xiaodongonline.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.xiaodongonline.R
import com.example.xiaodongonline.databinding.LoginFragmentBinding

/**
 *  author : 徐亚雄
 *  date : 2021/7/6 13:18
 *  description : 登录
 */

class LoginFragment : Fragment(R.layout.login_fragment) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: LoginFragmentBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginFragmentBinding.bind(view)
    }

}