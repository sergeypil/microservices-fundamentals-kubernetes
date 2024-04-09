{{/*
Current date helper
*/}}
{{- define "mychart.currentdate" -}}
{{- now | htmlDate -}}
{{- end -}}

{{/*
Version helper
*/}}
{{- define "mychart.version" -}}
{{- .Chart.AppVersion -}}
{{- end -}}