package com.mat.inspector

import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {

    viewModel { ConfigurationViewModel(get<FileRepository>()) }
    viewModel { ParametersViewModel(get()) }
}

val repositoriesModule = module {

    single { RemoteRepository() }
    single { FileRepository() }
}