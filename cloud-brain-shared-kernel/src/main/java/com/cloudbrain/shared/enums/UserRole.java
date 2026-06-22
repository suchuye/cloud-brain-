package com.cloudbrain.shared.enums;

/**
 * 系统角色（RBAC）。
 * <ul>
 *   <li>PATIENT     — 患者，可查看本人病历和挂号记录</li>
 *   <li>DOCTOR      — 医生，可接诊、开医嘱处方</li>
 *   <li>TECHNICIAN  — 医技人员，可上传影像/检验数据</li>
 *   <li>ADMIN       — 管理员，管理排班号源和系统配置</li>
 * </ul>
 */
public enum UserRole {
    PATIENT,
    DOCTOR,
    TECHNICIAN,
    ADMIN
}
