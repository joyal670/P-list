package com.ncomfortsagent.model.view_bank_details

data class AgentBankDetail(
    val accountname: String,
    val accountnumber: String,
    val bankname: String,
    val branchname: String,
    val id: Int,
    val ifsc: String
)