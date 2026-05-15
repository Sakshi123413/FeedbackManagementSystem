import api from "@/services/api"

export interface Feedback {
  id: number
  userName: string
  title: string
  message: string
  rating: number
  status: string
  date: string
}

export async function getFeedbacks(page = 0, size = 20): Promise<Feedback[]> {
  const response = await api.get("/feedback", {
    params: { page, size, sortBy: "createdAt", sortDir: "desc" },
  })
  const paged = response.data.data
  return (paged.content || []).map((f: Record<string, unknown>) => ({
    id: f.id,
    userName: f.userName,
    title: f.title,
    message: f.message,
    rating: f.rating,
    status: f.status,
    date: f.createdAt,
  }))
}

export async function submitFeedback(data: {
  title: string
  message: string
  rating: number
}): Promise<Feedback> {
  const response = await api.post("/feedback", data)
  const f = response.data.data
  return {
    id: f.id,
    userName: f.userName,
    title: f.title,
    message: f.message,
    rating: f.rating,
    status: f.status,
    date: f.createdAt,
  }
}
