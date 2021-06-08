package com.mat.inspector

import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.io.File

val viewModelsModule = module {

    viewModel { ConfigurationViewModel(get<FileRepository>(), Gson()) }
    viewModel { WritablesViewModel() }
}

val repositoriesModule = module {

    single { FileRepository() }
}