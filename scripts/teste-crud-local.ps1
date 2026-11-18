param([string]$BaseUrl="http://localhost:8080")
$ErrorActionPreference="Stop"
Write-Host "Teste E2E MetroSP - sera criado e removido um registro de cada tabela ESTACAO e OCORRENCIA"
$email=Read-Host "Email do administrador bootstrap"
$secure=Read-Host "Senha do administrador" -AsSecureString
$ptr=[Runtime.InteropServices.Marshal]::SecureStringToBSTR($secure)
try { $password=[Runtime.InteropServices.Marshal]::PtrToStringBSTR($ptr) }
finally { [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($ptr) }
$login = Invoke-RestMethod -Uri "$BaseUrl/api/auth/login" -Method Post -ContentType 'application/json' -Body (@{email=$email;senha=$password} | ConvertTo-Json)
Remove-Variable password
if ($login.perfil -ne 'ADMIN') { throw "O login precisa retornar perfil ADMIN" }
$headers=@{Authorization="Bearer $($login.token)"}
$stationId=$null
$occurrenceId=$null
try {
  # Estacao: CREATE, READ, UPDATE
  $est=@{nome='Estacao Teste CI';ordem=99;linha='Azul';ativa='S';sentido='JAB'}
  $newStation=Invoke-RestMethod -Uri "$BaseUrl/api/estacoes" -Method Post -Headers $headers -ContentType 'application/json' -Body ($est|ConvertTo-Json)
  $stationId=$newStation.id
  Invoke-RestMethod -Uri "$BaseUrl/api/estacoes/$stationId" -Headers $headers | Out-Null
  $est.nome='Estacao Teste Atualizada'
  Invoke-RestMethod -Uri "$BaseUrl/api/estacoes/$stationId" -Method Put -Headers $headers -ContentType 'application/json' -Body ($est|ConvertTo-Json) | Out-Null
  Write-Host "CREATE READ UPDATE Estacao ID $stationId OK"
  # Ocorrencia: CREATE READ UPDATE DELETE - vinculada a estacao e funcionario
  $oc=@{tipoProblema='Demonstracao';mensagem='Teste de CRUD do Azure DevOps';status='A';dataInicio=(Get-Date).ToString('yyyy-MM-ddTHH:mm:ss');dataFim=$null;estacaoId=$stationId;funcionarioId=$login.id}
  $newOcc=Invoke-RestMethod -Uri "$BaseUrl/api/ocorrencias" -Method Post -Headers $headers -ContentType 'application/json' -Body ($oc|ConvertTo-Json)
  $occurrenceId=$newOcc.id
  Invoke-RestMethod -Uri "$BaseUrl/api/ocorrencias/$occurrenceId" -Headers $headers | Out-Null
  $oc.mensagem='Demonstracao de alteracao PUT confirmada'
  Invoke-RestMethod -Uri "$BaseUrl/api/ocorrencias/$occurrenceId" -Method Put -Headers $headers -ContentType 'application/json' -Body ($oc|ConvertTo-Json) | Out-Null
  Write-Host "CREATE READ UPDATE Ocorrencia ID $occurrenceId OK"
  Invoke-RestMethod -Uri "$BaseUrl/api/ocorrencias/$occurrenceId" -Method Delete -Headers $headers | Out-Null
  $occurrenceId=$null
  Invoke-RestMethod -Uri "$BaseUrl/api/estacoes/$stationId" -Method Delete -Headers $headers | Out-Null
  $stationId=$null
  Write-Host 'DELETE Ocorrencia e Estacao OK. CRUD COMPLETO.'
} finally {
  if ($occurrenceId) { try { Invoke-RestMethod -Uri "$BaseUrl/api/ocorrencias/$occurrenceId" -Method Delete -Headers $headers | Out-Null } catch { Write-Warning "Falha de cleanup Ocorrencia" } }
  if ($stationId) { try { Invoke-RestMethod -Uri "$BaseUrl/api/estacoes/$stationId" -Method Delete -Headers $headers | Out-Null } catch { Write-Warning "Falha de cleanup Estacao" } }
}
