import { BPM_MAX, BPM_MIN, getSeverity } from "../types";

interface Props { bpm: number; onChange: (v: number) => void; }

const labels = { stable: "Estable", elevated: "Elevado", crisis: "Crisis" };

/** Panel externo de pruebas: simula el sensor de ritmo cardíaco */
export default function BPMSimulatorPanel({ bpm, onChange }: Props) {
  const sev = getSeverity(bpm);
  return (
    <aside className="w-full max-w-sm rounded-2xl border border-slate-700 bg-slate-900 p-5 text-slate-200">
      <h2 className="text-lg font-semibold">Simulador de sensor</h2>
      <p className="mt-1 text-sm text-slate-400">Mueve el control para simular el pulso del usuario.</p>
      <div className="mt-5 flex items-baseline justify-between">
        <span className="text-4xl font-bold tabular-nums">{bpm}<span className="ml-1 text-base font-normal text-slate-400">BPM</span></span>
        <span className={`rounded-full px-3 py-1 text-sm font-medium ${sev === "crisis" ? "bg-red-400/20 text-red-300" : sev === "elevated" ? "bg-amber-400/20 text-amber-300" : "bg-teal-400/20 text-teal-300"}`}>{labels[sev]}</span>
      </div>
      <input
        type="range" min={BPM_MIN} max={BPM_MAX} value={bpm}
        onChange={(e) => onChange(Number(e.target.value))}
        aria-label="Latidos por minuto simulados"
        className="mt-4 h-2 w-full cursor-pointer accent-teal-300"
      />
      <div className="mt-1 flex justify-between text-xs text-slate-500"><span>{BPM_MIN}</span><span>100</span><span>{BPM_MAX}</span></div>
      <ul className="mt-4 space-y-1 text-xs text-slate-400">
        <li>&lt; 80: tono sereno</li>
        <li>&gt; 100: Modo Crisis automático</li>
        <li>&gt; 110: tono imperativo y calmante</li>
      </ul>
    </aside>
  );
}
