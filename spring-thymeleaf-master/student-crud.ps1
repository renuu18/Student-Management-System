$apiUrl = if ($env:STUDENT_API_URL) {
    "$($env:STUDENT_API_URL.TrimEnd('/'))/api/v1/students"
} else {
    "http://localhost:8080/api/v1/students"
}

function Get-Students {
    $response = Invoke-RestMethod -Method Get -Uri $apiUrl
    if ($response -is [System.Array]) {
        return $response
    }
    if ($null -ne $response.value) {
        return $response.value
    }
    return @($response)
}

function Show-Students {
    Clear-Host
    Write-Host "STUDENTS FROM MYSQL" -ForegroundColor Cyan
    Write-Host "====================" -ForegroundColor Cyan
    $students = @(Get-Students)
    if ($students.Count -eq 0) {
        Write-Host "No students found."
    } else {
        $students | Select-Object id, firstName, lastName, email, age |
            Format-Table -AutoSize
    }
}

function Add-Student {
    $student = @{
        firstName = Read-Host "First name"
        lastName = Read-Host "Last name"
        email = Read-Host "Email"
        age = [int](Read-Host "Age")
    } | ConvertTo-Json

    Invoke-RestMethod -Method Post -Uri $apiUrl -ContentType "application/json" -Body $student | ConvertTo-Json
    Read-Host "Press Enter to continue"
}

function Update-Student {
    $id = Read-Host "Student ID to update"
    $student = @{
        firstName = Read-Host "New first name"
        lastName = Read-Host "New last name"
        email = Read-Host "New email"
        age = [int](Read-Host "New age")
    } | ConvertTo-Json

    Invoke-RestMethod -Method Put -Uri "$apiUrl/$id" -ContentType "application/json" -Body $student | ConvertTo-Json
    Read-Host "Press Enter to continue"
}

function Remove-Student {
    $id = Read-Host "Student ID to delete"
    $answer = Read-Host "Type DELETE to confirm"
    if ($answer -eq "DELETE") {
        Invoke-RestMethod -Method Delete -Uri "$apiUrl/$id"
        Write-Host "Student $id deleted."
    } else {
        Write-Host "Delete cancelled."
    }
    Read-Host "Press Enter to continue"
}

while ($true) {
    Show-Students
    Write-Host ""
    Write-Host "1. Insert student"
    Write-Host "2. Update student"
    Write-Host "3. Delete student"
    Write-Host "4. Refresh list"
    Write-Host "5. Exit"
    $choice = Read-Host "Choose an option"

    switch ($choice) {
        "1" { Add-Student }
        "2" { Update-Student }
        "3" { Remove-Student }
        "4" { }
        "5" { break }
        default { Write-Host "Invalid option"; Read-Host "Press Enter to continue" }
    }
}
