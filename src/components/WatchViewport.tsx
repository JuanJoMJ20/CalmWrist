import { ReactNode } from "react";
import { motion } from "framer-motion";
import type { Severity } from "../types";

interface Props {
  children: ReactNode;
  haptic: boolean;
  severity: Severity;
}

const glow: Record<Severity, string> = {
  stable: "rgba(125,211,192,0.55)",
  elevated: "rgba(244,185,120,0.6)",
  crisis: "rgba(255,120,120,0.7)",
};

/** Emulador: marco metálico circular 320x320 + pantalla OLED #000 */
export default function WatchViewport({ children, haptic, severity }: Props) {
  return (
    <div className="relative flex items-center justify-center">
      {/* Corona lateral */}
      <div className="absolute -right-2 top-1/2 h-14 w-3 -translate-y-1/2 rounded-r-md bg-gradient-to-b from-zinc-500 via-zinc-300 to-zinc-600" />
      <motion.div
        // Simulación háptica: shake del marco + resplandor del borde
        animate={
          haptic
            ? { x: [0, -3, 3, -2, 2, 0], boxShadow: [`0 0 0 0 ${glow[severity]}`, `0 0 36px 10px ${glow[severity]}`, `0 0 0 0 ${glow[severity]}`] }
            : { x: 0, boxShadow: "0 0 0 0 rgba(0,0,0,0)" }
        }
        transition={haptic ? { duration: 1, repeat: Infinity, ease: "easeInOut" } : { duration: 0.3 }}
        className="h-[320px] w-[320px] rounded-full bg-gradient-to-br from-zinc-200 via-zinc-500 to-zinc-800 p-[10px]"
      >
        <div className="h-full w-full overflow-hidden rounded-full bg-black p-[3px] ring-2 ring-zinc-900">
          <div className="relative h-full w-full overflow-hidden rounded-full bg-[#000000]">{children}</div>
        </div>
      </motion.div>
    </div>
  );
}
