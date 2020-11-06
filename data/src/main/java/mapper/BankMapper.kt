package mapper

import br.com.noclaftech.domain.model.Bank
import response.BankResponse
import javax.inject.Inject

class BankMapper @Inject constructor(){
    fun map(responseList: List<BankResponse>?) : List<Bank>?{
        if (responseList == null)
            return null

        return responseList.map{(map(it)!!)}
    }

    fun map(response : BankResponse?) : Bank? {
        if (response == null)
            return null

        return Bank(
            id = response.id,
            owner = response.owner,
            bank = response.bank,
            agency = response.agency,
            account = response.account,
            cpf = response.cpf,
            cnpj = response.cnpj,
            accountType = response.account_type
        )
    }
}