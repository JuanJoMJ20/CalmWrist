import { motion } from "framer-motion";
import type { Severity } from "../types";

interface Props { bpm: number; severity: Severity; onTrigger: () => void; }

/** Vista de reposo: un solo target gigante (≥80px) de fricción cero */
export default function PanicButtonScreen({ bpm, severity, onTrigger }: Props) {
  const color = severity === "crisis" ? "#ff8a8a" : severity === "elevated" ? "#f4b978" : "#7dd3c0";
  const message =
    severity === "stable" ? "Ritmo estable. Todo está bien." :
    severity === "crisis" ? "Estoy contigo. Toca para respirar." :
    "Tu pulso sube. Estoy aquí.";
  return (
    <div className="flex h-full w-full flex-col items-center justify-center gap-2">
      <p className="text-[13px] text-zinc-400" aria-live="polite">{message}</p>
      <motion.button
        onClick={onTrigger}
        whileTap={{ scale: 0.92 }}
        animate={{ scale: [1, 1.04, 1] }}
        transition={{ duration: 60 / bpm, repeat: Infinity, ease: "easeInOut" }} // late al ritmo real
        aria-label="Pedir ayuda ahora"
        style={{ borderColor: color, boxShadow: `0 0 28px ${color}55` }}
        className="flex h-[170px] w-[170px] flex-col items-center justify-center rounded-full border-4 bg-black text-white focus:outline-none focus-visible:ring-4 focus-visible:ring-white/60"
      >
        <span className="text-5xl font-bold tabular-nums" style={{ color }}>{bpm}</span>
        <span className="text-xs text-zinc-400">BPM</span>
        <span className="mt-2 text-sm font-semibold">Ayuda</span>
      </motion.button>
    </div>
  );
}
